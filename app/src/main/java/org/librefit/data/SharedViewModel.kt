package org.librefit.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.librefit.MainApplication
import org.librefit.db.Exercise
import org.librefit.db.Workout
import org.librefit.db.WorkoutWithExercises

class SharedViewModel : ViewModel() {
    /**
     * A list used by CreateRoutineScreen and AddExerciseScreen
     */

    private val selectedExercisesList = mutableStateListOf<ExerciseDC>()

    var addedExercisesList : List<ExerciseDC> = selectedExercisesList


    fun addExerciseList (exerciseList: List<ExerciseDC>){
        selectedExercisesList += exerciseList
    }

    fun removeExercise ( exercise: ExerciseDC ){
        selectedExercisesList.remove(exercise)
    }

    fun resetList () {
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


    /**
     * Database is loaded by [MainApplication] at startup
     */

    private val workoutDao = MainApplication.workoutDatabase.getWorkoutDao()

    val workoutList : LiveData<List<Workout>> = workoutDao.getWorkouts()

    fun addWorkout(workout: Workout){
        viewModelScope.launch(Dispatchers.IO){
            workoutDao.addWorkout(workout)
        }
    }

    fun deleteWorkout(workout: Workout){
        viewModelScope.launch(Dispatchers.IO){
            workoutDao.deleteWorkout(workout)
        }
    }

    fun addExercise(exercise: Exercise) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutDao.addExercise(exercise)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch(Dispatchers.IO) {
            workoutDao.deleteExercise(exercise)
        }
    }

    fun getWorkoutWithExercises(workoutId: Int): LiveData<WorkoutWithExercises> {
        val workoutWithExercises = MutableLiveData<WorkoutWithExercises>()
        viewModelScope.launch(Dispatchers.IO) {
            val data = workoutDao.getWorkoutWithExercises(workoutId)
            workoutWithExercises.postValue(data)
        }
        return workoutWithExercises
    }

    fun addWorkoutWithExercises(workout: Workout, exercises: List<ExerciseDC>) {
        val list = exercises.toList()
        viewModelScope.launch(Dispatchers.IO) {
            workoutDao.addWorkoutWithExercises(workout, list)
        }
    }
}