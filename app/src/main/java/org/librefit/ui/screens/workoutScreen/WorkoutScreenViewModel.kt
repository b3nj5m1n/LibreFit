package org.librefit.ui.screens.workoutScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class WorkoutScreenViewModel : ViewModel() {
    private var completedSets by mutableFloatStateOf(0F)
    private var totalSets by mutableFloatStateOf(0F)

    fun getProgress(): Float {
        return completedSets / totalSets
    }

    fun addCompletedSet(isCompleted: Boolean){
        if (isCompleted) completedSets++ else completedSets--
    }

    fun addTotalSet(){
        totalSets++
    }

}