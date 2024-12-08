package org.librefit.util

import androidx.compose.runtime.Immutable
import org.librefit.db.Set
import org.librefit.enums.SetMode

@Immutable
data class ExerciseWithSets(
    val id: Int = 0,
    val exerciseId: Int = 0,
    val exerciseDC: ExerciseDC,
    val sets: List<Set> = emptyList<Set>(),
    val note: String = "",
    val setMode: SetMode = SetMode.WEIGHT
)