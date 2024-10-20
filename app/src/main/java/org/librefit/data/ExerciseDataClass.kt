package org.librefit.data

data class ExerciseDC(
    val id: String,
    val name: String,
    val force: Force? = null,
    val level: Level,
    val mechanic: Mechanic? = null,
    val equipment: Equipment? = null,
    val primaryMuscles: List<Muscle>,
    val secondaryMuscles: List<Muscle>,
    val instructions: List<String>,
    val category: Category,
    val images: List<String>
)

enum class Force {
    STATIC,
    PULL,
    PUSH
}

enum class Level {
    BEGINNER,
    INTERMEDIATE,
    EXPERT
}

enum class Mechanic {
    ISOLATION,
    COMPOUND
}

enum class Equipment {
    MEDICINE_BALL,
    DUMBBELL,
    BODY_ONLY,
    BANDS,
    KETTLEBELLS,
    FOAM_ROLL,
    CABLE,
    MACHINE,
    BARBELL,
    EXERCISE_BALL,
    E_Z_CURL_BAR,
    OTHER
}

enum class Muscle {
    ABDOMINALS,
    ABDUCTORS,
    ADDUCTORS,
    BICEPS,
    CALVES,
    CHEST,
    FOREARMS,
    GLUTES,
    HAMSTRINGS,
    LATS,
    LOWER_BACK,
    MIDDLE_BACK,
    NECK,
    QUADRICEPS,
    SHOULDERS,
    TRAPS,
    TRICEPS
}

enum class Category {
    POWERLIFTING,
    STRENGTH,
    STRETCHING,
    CARDIO,
    OLYMPIC_WEIGHTLIFTING,
    STRONGMAN,
    PLYOMETRICS
}