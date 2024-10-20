package org.librefit.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.librefit.ui.screens.createRoutine.ExerciseWithSets

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY title" )
    fun getWorkouts() : Flow<List<Workout>>

    @Insert
    fun addWorkout(workout: Workout) : Long

    @Delete
    fun deleteWorkout(workout: Workout)

    @Insert
    fun addExercise(exercise: Exercise) : Long

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Insert
    suspend fun addSet(set: Set)

    @Delete
    suspend fun deleteSet(set: Set)

    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId")
    suspend fun getExercisesFromWorkout(workoutId: Int): List<Exercise>

    @Query("SELECT * FROM sets WHERE exerciseId = :exerciseId")
    suspend fun getSetsFromExercise(exerciseId : Int): List<Set>

    @Transaction
    suspend fun addWorkoutWithExercises(workout: Workout, exercises: List<ExerciseWithSets>) {
        val workoutId = addWorkout(workout).toInt()
        exercises.forEach {
            val exerciseId = addExercise(Exercise(exerciseId = it.exercise.id, notes = it.note, workoutId = workoutId))
            it.sets.forEach { set ->
                addSet(set.copy(exerciseId = exerciseId.toInt()))
            }
        }
    }
}