/*
 * Copyright (c) 2025. LibreFit
 *
 * This file is part of LibreFit
 *
 * LibreFit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreFit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreFit.  If not, see <https://www.gnu.org/licenses/>.
 *
 * LibreFit is subject to additional terms covering author attribution and
 * trademark usage, as found in the accompanying ADDITIONAL_TERMS.md file.
 */

package org.librefit.ui.models

import androidx.compose.runtime.Immutable
import org.librefit.enums.WorkoutState
import java.time.LocalDateTime
import kotlin.random.Random

/**
 * The [org.librefit.db.entity.Workout] model used only by the ui. The difference is the use
 * of [Immutable] annotation
 *
 * @see [org.librefit.db.entity.Workout]
 */
@Immutable
data class UiWorkout(
    val id: Long = 0,
    val routineId: Long = Random.Default.nextLong(),
    val notes: String = "",
    val title: String = "",
    val state: WorkoutState = WorkoutState.COMPLETED,
    val timeElapsed: Int = 0,
    val created: LocalDateTime = LocalDateTime.now(),
    val completed: LocalDateTime = LocalDateTime.now()
)