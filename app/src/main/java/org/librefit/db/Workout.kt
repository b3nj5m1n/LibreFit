package org.librefit.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Workout(
    @PrimaryKey(true) val id : Int = 0,
    val title : String
)
