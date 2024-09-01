package org.librefit.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val selectedExercisesList = mutableStateListOf<Exercise>()

    var addedExercisesList : List<Exercise> = selectedExercisesList


    fun addExerciseList (exerciseList: List<Exercise>){
        selectedExercisesList += exerciseList
    }

    fun removeExercise ( exercise: Exercise ){
        selectedExercisesList.remove(exercise)
    }

    fun resetList () {
        selectedExercisesList.clear()
    }


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