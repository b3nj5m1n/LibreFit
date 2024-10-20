package org.librefit.nav

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.librefit.data.ExerciseDC
import org.librefit.data.SharedViewModel
import org.librefit.ui.screens.AboutScreen
import org.librefit.ui.screens.AddExerciseScreen
import org.librefit.ui.screens.createRoutine.CreateRoutineScreen
import org.librefit.ui.screens.MainScreen
import org.librefit.ui.screens.SettingsScreen
import org.librefit.ui.screens.workout.WorkoutScreen
import org.librefit.data.DataStoreManager

@Composable
fun NavigationHost(list: List<ExerciseDC>, userPreferences: DataStoreManager) {

    val navController = rememberNavController()

    val sharedViewModel : SharedViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Destination.MainScreen,
        enterTransition = { scaleIn(tween(450), 0.8f) + fadeIn(tween(400)) },
        exitTransition = { scaleOut(tween(450), 1.2f)  },
        popEnterTransition = { scaleIn(tween(450),1.2f)  },
        popExitTransition = { scaleOut(tween(450), 0.8f) + fadeOut(tween(400)) }
    ){
        composable<Destination.MainScreen> {
            MainScreen(navController = navController)
        }
        composable<Destination.CreateRoutineScreen> {
            CreateRoutineScreen(
                sharedViewModel = sharedViewModel,
                navigateBack = { navController.popBackStack() },
                navigateAddExercise = { navController.navigate(Destination.AddExerciseScreen )}
            )
        }
        composable<Destination.AddExerciseScreen> {
            AddExerciseScreen(
                list = list,
                navigateBack = { navController.popBackStack() },
                viewModel = sharedViewModel
            )
        }
        composable<Destination.SettingsScreen> {
            SettingsScreen(
                userPreferences = userPreferences,
                navigateBack = { navController.popBackStack() }
            )
        }
        composable<Destination.AboutScreen> {
            AboutScreen(navigateBack = { navController.popBackStack() })
        }
        composable<Destination.WorkoutScreen> {
            WorkoutScreen(
                userPreferences = userPreferences,
                workoutId = it.toRoute<Destination.WorkoutScreen>().workoutId,
                navController = navController,
                list = list
            )
        }
    }
}