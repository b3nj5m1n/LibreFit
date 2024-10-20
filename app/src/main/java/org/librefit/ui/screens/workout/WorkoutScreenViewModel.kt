package org.librefit.ui.screens.workout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.librefit.MainApplication
import org.librefit.db.Exercise
import org.librefit.db.Set
import org.librefit.ui.screens.createRoutine.ExerciseWithSets

class WorkoutScreenViewModel : ViewModel() {
    val totalSets: Int
        get() = exercisesWithSets.value.sumOf { it.sets.size }

    private var completedSets by mutableFloatStateOf(0F)

    fun addCompletedSet(completed: Boolean) {
        if (completed) {
            completedSets++
        } else {
            completedSets--
        }
    }

    val progress: Float
        get() = completedSets / totalSets


    private val _exercisesWithSets = MutableStateFlow<List<ExerciseWithSets>>(emptyList())
    val exercisesWithSets = _exercisesWithSets.asStateFlow()
    private var currentId: Int = 0

    fun addExerciseWithSets(exerciseWithSets: ExerciseWithSets) {
        val newExerciseWithSets = exerciseWithSets.copy(id = currentId++)
        _exercisesWithSets.value += newExerciseWithSets
    }

    fun addSetToExercise(index : Int) {
        _exercisesWithSets.value = _exercisesWithSets.value.map { exerciseWithSets ->
            if (exerciseWithSets.id == index) {
                exerciseWithSets.copy(sets = exerciseWithSets.sets + Set(exerciseId = 0))
            } else {
                exerciseWithSets
            }
        }
    }

    fun updateSet(exerciseId: Int, set: Set, weight: Int = -1, reps: Int = -1) {
        _exercisesWithSets.value = _exercisesWithSets.value.map { exerciseWithSets ->
            if (exerciseWithSets.id == exerciseId) {
                val updatedSets = exerciseWithSets.sets.map { currentSet ->
                    if (currentSet == set) {
                        currentSet.copy(
                            weight = if (currentSet.weight != weight && weight != -1) weight else currentSet.weight,
                            reps = if (currentSet.reps != reps && reps != -1) reps else currentSet.reps
                        )
                    } else {
                        currentSet
                    }
                }
                exerciseWithSets.copy(sets = updatedSets)
            } else {
                exerciseWithSets
            }
        }
    }


    private val workoutDao = MainApplication.workoutDatabase.getWorkoutDao()
    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises

    fun getExercisesFromWorkout(workoutId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = workoutDao.getExercisesFromWorkout(workoutId)
            _exercises.value = data
        }

    }
    fun getSetsFromExercise(exerciseId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = workoutDao.getSetsFromExercise(exerciseId)
            val updatedExercisesWithSets = _exercisesWithSets.value.map { exerciseWithSets ->
                if (exerciseWithSets.exerciseId == exerciseId) {
                    exerciseWithSets.copy(sets = data)
                } else {
                    exerciseWithSets
                }
            }
            _exercisesWithSets.value = updatedExercisesWithSets
        }
    }
}
