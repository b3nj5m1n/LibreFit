package org.librefit.util

import org.librefit.db.Set
import org.librefit.enums.SetMode

data class ExerciseWithSets(
    val id: Int = 0,
    val exerciseId: Int = 0,
    val exerciseDC: ExerciseDC,
    val sets: List<Set> = emptyList<Set>(),
    val note: String = "",
    val restTime: Int = 0,
    val setMode: SetMode = SetMode.WEIGHT
)