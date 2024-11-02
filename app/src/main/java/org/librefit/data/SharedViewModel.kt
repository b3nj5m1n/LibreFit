/*
 * Copyright (c) 2024 LibreFit
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
 */

package org.librefit.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    /**
     * A list used by CreateRoutineScreen and AddExerciseScreen
     */

    private val selectedExercisesList = mutableStateListOf<ExerciseDC>()

    fun getSelectedExercisesList(): List<ExerciseDC> {
        val list = selectedExercisesList.toList()
        selectedExercisesList.clear()
        return list
    }

    fun addSelectedExerciseToList(exerciseList: List<ExerciseDC>) {
        resetSelectedExercisesList()
        selectedExercisesList += exerciseList
    }

    fun resetSelectedExercisesList() {
        selectedExercisesList.clear()
    }


    /**
     * A list used only by AddExerciseScreen based on FiltersCard
     */

    private var filtersList = mutableStateListOf<Enum<*>>()

    init {
        initializeFilterList()
    }

    private fun initializeFilterList() {
        filtersList.addAll(Level.entries)
        filtersList.addAll(Force.entries)
        filtersList.addAll(Mechanic.entries)
        filtersList.addAll(Equipment.entries)
        filtersList.addAll(Muscle.entries)
        filtersList.addAll(Category.entries)
    }

    fun addEnumToFilter(enum: Enum<*>) {
        filtersList.add(enum)
    }

    fun removeEnumFromFilter(enum: Enum<*>) {
        filtersList.remove(enum)
    }

    fun isEnumInFilter(enum: Enum<*>): Boolean {
        return filtersList.contains(enum)
    }

    fun resetFilterList() {
        filtersList.clear()
        initializeFilterList()
    }

    fun filter(exercise: ExerciseDC): Boolean {
        if (!filtersList.contains(exercise.level)) {
            return false
        }
        if (exercise.force != null && !filtersList.contains(exercise.force)) {
            return false
        }
        if (exercise.mechanic != null && !filtersList.contains(exercise.mechanic)) {
            return false
        }
        if (exercise.equipment != null && !filtersList.contains(exercise.equipment)) {
            return false
        }
        if (!filtersList.containsAll(exercise.primaryMuscles) || !filtersList.containsAll(exercise.secondaryMuscles)) {
            return false
        }
        if (!filtersList.contains(exercise.category)) {
            return false
        }
        return true
    }
}