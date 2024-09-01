package org.librefit.util

import org.librefit.data.Category
import org.librefit.data.Equipment
import org.librefit.data.Force
import org.librefit.data.Level
import org.librefit.data.Mechanic
import org.librefit.data.Muscle


fun stringToEnum(string : String) : Enum<*>? {
    val result = when(string){
        "Static" -> Force.STATIC
        "Pull" -> Force.PULL
        "Push" -> Force.PUSH
        "Beginner" -> Level.BEGINNER
        "Intermediate" -> Level.INTERMEDIATE
        "Expert" -> Level.EXPERT
        "Isolation" -> Mechanic.ISOLATION
        "Compound" -> Mechanic.COMPOUND
        "Medicine ball" -> Equipment.MEDICINE_BALL
        "Dumbbell" -> Equipment.DUMBBELL
        "Body only" -> Equipment.BODY_ONLY
        "Bands" -> Equipment.BANDS
        "Kettlebells" -> Equipment.KETTLEBELLS
        "Foam roll" -> Equipment.FOAM_ROLL
        "Cable" -> Equipment.CABLE
        "Machine" -> Equipment.MACHINE
        "Barbell" -> Equipment.BARBELL
        "Exercise ball" -> Equipment.EXERCISE_BALL
        "E-z curl bar" -> Equipment.E_Z_CURL_BAR
        "Other" -> Equipment.OTHER
        "Abdominals" -> Muscle.ABDOMINALS
        "Abductors" -> Muscle.ABDUCTORS
        "Adductors" -> Muscle.ADDUCTORS
        "Biceps" -> Muscle.BICEPS
        "Calves" -> Muscle.CALVES
        "Chest" -> Muscle.CHEST
        "Forearms" -> Muscle.FOREARMS
        "Glutes" -> Muscle.GLUTES
        "Hamstrings" -> Muscle.HAMSTRINGS
        "Lats" -> Muscle.LATS
        "Lower back" -> Muscle.LOWER_BACK
        "Middle back" -> Muscle.MIDDLE_BACK
        "Neck" -> Muscle.NECK
        "Quadriceps" -> Muscle.QUADRICEPS
        "Shoulders" -> Muscle.SHOULDERS
        "Traps" -> Muscle.TRAPS
        "Triceps" -> Muscle.TRICEPS
        "Powerlifting" -> Category.POWERLIFTING
        "Strength" -> Category.STRENGTH
        "Stretching" -> Category.STRETCHING
        "Cardio" -> Category.CARDIO
        "Olympic weightlifting" -> Category.OLYMPIC_WEIGHTLIFTING
        "Strongman" -> Category.STRONGMAN
        "Plyometrics" -> Category.PLYOMETRICS
        else -> null
    }
    return result
}



