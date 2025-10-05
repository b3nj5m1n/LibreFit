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

package org.librefit.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.serialization.Serializable
import org.librefit.db.entity.Exercise
import org.librefit.db.entity.ExerciseDC
import org.librefit.db.entity.Set

/**
 * A data class representing an [Exercise] with its associated [Set]s.
 *
 * This class is used by Room to retrieve all the data associated with an exercise and
 * the sets associated with it. The actual dataset is stored as [ExerciseDC] and provided
 * by [org.librefit.db.repository.DatasetRepository]
 *
 * @property exercise It contains the user related data associated with this [Exercise].
 * @property sets The list of [Set] associated with the [exercise] containing all the user related data.
 * @property exerciseDC The actual features of the exercise itself. Details at [ExerciseDC]
 */
@Serializable
data class ExerciseWithSets(
    @Embedded val exercise: Exercise = Exercise(),
    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseId"
    )
    val sets: List<Set> = listOf(Set()),
    @Relation(
        parentColumn = "idExerciseDC",
        entityColumn = "id"
    )
    val exerciseDC: ExerciseDC = ExerciseDC()
)