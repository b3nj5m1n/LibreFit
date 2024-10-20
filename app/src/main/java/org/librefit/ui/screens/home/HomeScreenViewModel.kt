package org.librefit.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.librefit.MainApplication
import org.librefit.db.Workout

class HomeScreenViewModel : ViewModel() {
    private val workoutDao = MainApplication.workoutDatabase.getWorkoutDao()
    var workoutList: MutableState<List<Workout>> = mutableStateOf(emptyList())

    init {
        getWorkoutList()
    }

    private fun getWorkoutList() {
        viewModelScope.launch {
            workoutDao.getWorkouts().collect { workouts ->
                workoutList.value = workouts
            }
        }
    }

    fun deleteWorkout(workout: Workout){
        viewModelScope.launch(Dispatchers.IO){
            workoutDao.deleteWorkout(workout)
        }
    }
}