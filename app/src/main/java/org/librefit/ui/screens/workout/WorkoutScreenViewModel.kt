/*
 * Copyright (c) 2024 LibreFit
 *
 * This file is part of LibreFit
 *
 * LibreFit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreFit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreFit.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.librefit.ui.screens.workout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.librefit.MainApplication
import org.librefit.db.Set
import org.librefit.db.Workout
import org.librefit.enums.SetMode
import org.librefit.util.ExerciseDC
import org.librefit.util.ExerciseWithSets
import kotlin.random.Random

class WorkoutScreenViewModel(
    workoutId: Int,
    private val list: List<ExerciseDC>
) : ViewModel() {
    val exercises = mutableStateListOf<ExerciseWithSets>()

    fun getExercises(): List<ExerciseWithSets> {
        return exercises.toList()
    }

    fun addExerciseWithSets(exerciseWithSets: ExerciseWithSets) {
        val newExerciseWithSets = exerciseWithSets.copy(
            id = Random.nextInt(),
            sets = if (exerciseWithSets.sets.isEmpty()) {
                listOf(Set(id = Random.nextInt()))
            } else exerciseWithSets.sets
        )
        exercises.add(newExerciseWithSets)
    }

    fun addSetToExercise(exercise: ExerciseWithSets) {
        val index = exercises.indexOf(exercise)
        exercises[index] = exercise.copy(sets = exercise.sets + listOf(Set(id = Random.nextInt())))
    }

    /**
     * It updates [Set] by assigning a [value] to one attribute based on [mode].
     * @param exercise The [ExerciseWithSets] that has the set to update
     * @param set [Set] to update
     * @param value The new value to assign to one attribute of [Set]
     * @param mode Defines which attribute should the value be assigned.
     * Based on which attribute you want to change, you have to pass the corresponding value:
     *  [Set.weight]        -> 0;
     *  [Set.reps]          -> 1;
     *  [Set.elapsedTime]   -> 2;
     *  [Set.completed]     -> 3
     */
    fun updateSet(exercise: ExerciseWithSets, set: Set, value: Int, mode: Int) {
        val index = exercises.indexOf(exercise)
        exercises[index] = exercise.copy(
            sets = exercise.sets.map {
                if (it.id == set.id) {
                    when (mode) {
                        0 -> set.copy(weight = value)
                        1 -> set.copy(reps = value)
                        2 -> set.copy(elapsedTime = value)
                        3 -> set.copy(completed = value == 1)
                        else -> set
                    }
                } else it
            }
        )
    }

    /**
     * It updates [ExerciseWithSets] by assigning a [value] to one attribute based on [mode].
     * @param exercise The [ExerciseWithSets] to update
     * @param value The new value to assign to one attribute of [ExerciseWithSets]
     * @param mode Defines which attribute should the [value] be assigned.
     * Based on which attribute you want to change, you have to pass the corresponding value:
     *  [ExerciseWithSets.note]    -> 0;
     *  [ExerciseWithSets.setMode] -> 1;
     */
    fun updateExercise(exercise: ExerciseWithSets, value: String, mode: Int) {
        val index = exercises.indexOf(exercise)
        exercises[index] = when (mode) {
            0 -> exercise.copy(note = value.toString())
            1 -> exercise.copy(
                setMode = when (value) {
                    SetMode.WEIGHT.name -> SetMode.WEIGHT
                    SetMode.TIME.name -> SetMode.TIME
                    SetMode.REPS.name -> SetMode.REPS
                    else -> SetMode.WEIGHT
                }
            )

            else -> exercise
        }
    }

    fun deleteExercise(exerciseWithSets: ExerciseWithSets) {
        exercises.remove(exerciseWithSets)
    }

    fun isListEmpty(): Boolean {
        return exercises.isEmpty()
    }

    fun getProgress(): Float {
        return exercises.sumOf {
            it.sets.filter { it.completed == true }.size
        }.toFloat() / exercises.sumOf { it.sets.size }
    }


    private val workoutDao = MainApplication.workoutDatabase.getWorkoutDao()

    init {
        getExercisesFromWorkout(workoutId)
    }

    fun getExercisesFromWorkout(workoutId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val exercises = workoutDao.getExercisesFromWorkout(workoutId)
            exercises.forEach { exercise ->
                val exerciseDC = list.associateBy { it.id }[exercise.exerciseId]
                if (exerciseDC != null) {
                    getSetsFromExercise(exercise.id)

                    addExerciseWithSets(
                        ExerciseWithSets(
                            exerciseDC = exerciseDC,
                            exerciseId = exercise.id,
                            note = exercise.notes,
                            setMode = exercise.setMode
                        )
                    )
                }
            }
        }
    }

    fun getSetsFromExercise(exerciseId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val sets = workoutDao.getSetsFromExercise(exerciseId)
            val exercise = exercises.find { it.exerciseId == exerciseId }!!
            val index = exercises.indexOf(exercise)
            exercises[index] = exercise.copy(sets = sets.map { it.copy(id = Random.nextInt()) })
        }
    }

    fun saveExercisesWithWorkout(workout: Workout, exercises: List<ExerciseWithSets>) {
        val list = exercises.toList()
        viewModelScope.launch(Dispatchers.IO) {
            workoutDao.addWorkoutWithExercises(workout, list)
        }
    }

    var timeElapsed by mutableIntStateOf(0)
        private set
    var isTimerRunning by mutableStateOf(true)
        private set
    private var pulsingText by mutableIntStateOf(0)


    init {
        startTimer()
    }

    fun startTimer() {
        isTimerRunning = true
        val startTime = System.currentTimeMillis()
        val pastTimeElapsed = timeElapsed

        viewModelScope.launch(Dispatchers.IO) {
            while (isTimerRunning) {
                val currentTime = System.currentTimeMillis()

                timeElapsed = (currentTime - startTime).toInt() / 1000 + pastTimeElapsed
                delay(1000)
            }
        }
    }

    fun stopTimer() {
        isTimerRunning = false

        viewModelScope.launch(Dispatchers.IO) {
            while (!isTimerRunning) {
                pulsingText++
                delay(600)
            }
        }
    }

    fun pulsingTimer(): Boolean {
        return !isTimerRunning && pulsingText % 2 == 1
    }
}
