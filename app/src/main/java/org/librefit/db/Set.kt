package org.librefit.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["exerciseId"])]
)
data class Set(
    @PrimaryKey(true) val id: Int = 0,
    val weight: Int? = null,
    val reps: Int? = null,
    val elapsedTime: Int? = null,
    val exerciseId: Int // Foreign key reference to Exercise
)