package org.librefit

import android.app.Application
import androidx.room.Room
import org.librefit.db.WorkoutDatabase

class MainApplication : Application() {
    companion object{
        lateinit var workoutDatabase: WorkoutDatabase
    }

    override fun onCreate() {
        super.onCreate()
        workoutDatabase = Room.databaseBuilder(
            applicationContext,
            WorkoutDatabase::class.java,
            WorkoutDatabase.NAME
        ).build()
    }
}