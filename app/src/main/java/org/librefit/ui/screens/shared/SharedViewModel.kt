/*
 * Copyright (c) 2024-2025. LibreFit
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

package org.librefit.ui.screens.shared

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.librefit.MainApplication
import org.librefit.db.Workout
import org.librefit.util.ExerciseDC
import org.librefit.util.ExerciseWithSets
import kotlin.random.Random

class SharedViewModel : ViewModel() {
    private val selectedExercisesList = mutableStateListOf<ExerciseDC>()

    fun getSelectedExercisesList(): List<ExerciseDC> {
        val list = selectedExercisesList.toList()
        selectedExercisesList.clear()
        return list
    }

    fun addSelectedExerciseToList(exerciseList: List<ExerciseDC>) {
        resetSelectedExercisesList()
        selectedExercisesList += exerciseList
    }

    fun resetSelectedExercisesList() {
        selectedExercisesList.clear()
    }



    private var passedWorkout = Workout()
    private var passedExercises = listOf<ExerciseWithSets>()
    private var passedRoutine = Workout()
    private var workoutId = 0


    fun updateWorkoutId(workoutId: Int) {
        this.workoutId = workoutId
        getDataFromDB()
    }

    fun setPassedData(
        workout: Workout? = null,
        exercises: List<ExerciseWithSets>,
        routine: Workout? = null
    ) {
        if (workout != null) {
            passedWorkout = workout
        }
        passedExercises = exercises
        if (routine != null) {
            passedRoutine = routine
        }
    }

    fun getPassedWorkout(): Workout {
        return passedWorkout
    }

    fun getPassedExercises(): List<ExerciseWithSets> {
        return passedExercises
    }

    fun getPassedRoutine(): Workout {
        return passedRoutine
    }


    private val workoutDao = MainApplication.workoutDatabase.getWorkoutDao()

    private fun getDataFromDB() {
        if (workoutId != 0) {
            viewModelScope.launch(Dispatchers.IO) {
                // Retrieves exercises from db and parse them to ExerciseWithSets
                val exercises = workoutDao.getExercisesFromWorkout(workoutId)
                passedExercises = exercises.map { exercise ->
                    ExerciseWithSets(
                        id = Random.nextInt(),
                        exerciseDC = MainApplication.exercisesList.associateBy { it.id }[exercise.exerciseId]!!,
                        exerciseId = exercise.id,
                        note = exercise.notes,
                        sets = workoutDao.getSetsFromExercise(exercise.id),
                        setMode = exercise.setMode,
                        restTime = exercise.restTime
                    )
                }
            }
            viewModelScope.launch(Dispatchers.IO) {
                passedWorkout = workoutDao.getWorkout(workoutId)

                passedRoutine = if (passedWorkout.routine) {
                    passedWorkout
                } else {
                    runCatching { workoutDao.getRoutines().first() }
                        .getOrDefault(emptyList())
                        .find { it.workoutId == passedWorkout.workoutId }
                        .takeIf { it?.id != passedWorkout.id } ?: Workout()
                }

            }
        } else {
            passedWorkout = Workout()
            passedRoutine = Workout()
            passedExercises = emptyList()
        }
    }
}