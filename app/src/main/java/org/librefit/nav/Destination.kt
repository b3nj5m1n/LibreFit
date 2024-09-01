package org.librefit.nav

import kotlinx.serialization.Serializable


sealed class Destination {
    @Serializable
    object MainScreen

    @Serializable
    object CreateRoutineScreen

    @Serializable
    object AddExerciseScreen

    @Serializable
    object SettingsScreen

    @Serializable
    object AboutScreen

    @Serializable
    object WorkoutScreen
}