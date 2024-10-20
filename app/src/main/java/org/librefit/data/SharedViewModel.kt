package org.librefit.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    /**
     * A list used by CreateRoutineScreen and AddExerciseScreen
     */

    private val selectedExercisesList = mutableStateListOf<ExerciseDC>()

    fun getSelectedExercisesList() : List<ExerciseDC> {
        return selectedExercisesList.toList()
    }

    fun addSelectedExerciseToList (exerciseList: List<ExerciseDC>){
        resetSelectedExercisesList()
        selectedExercisesList += exerciseList
    }

    fun removeExerciseFromList (exercise: ExerciseDC ){
        selectedExercisesList.remove(exercise)
    }

    fun resetSelectedExercisesList () {
        selectedExercisesList.clear()
    }


    /**
     * A list used only by AddExerciseScreen based on FiltersCard
     */

    private var filtersList = mutableStateListOf<Enum<*>>()

    init {
        initializeFilterList()
    }

    private fun initializeFilterList(){
        Level.entries.forEach { filtersList.add(it) }
        Force.entries.forEach { filtersList.add(it) }
        Level.entries.forEach { filtersList.add(it) }
        Mechanic.entries.forEach { filtersList.add(it) }
        Equipment.entries.forEach { filtersList.add(it) }
        Muscle.entries.forEach { filtersList.add(it) }
        Category.entries.forEach { filtersList.add(it) }
    }

    fun addEnum( enum : Enum<*> ){
        filtersList.add(enum)
    }

    fun removeEnum( enum : Enum<*> ){
        filtersList.remove(enum)
    }

    fun isEnumInList ( enum : Enum<*> ) : Boolean{
        return filtersList.contains(enum)
    }

    fun resetFilterList(){
        filtersList.clear()
        initializeFilterList()
    }
}