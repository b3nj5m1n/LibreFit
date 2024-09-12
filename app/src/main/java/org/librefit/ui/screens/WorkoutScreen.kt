package org.librefit.ui.screens

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import org.librefit.R
import org.librefit.data.SharedViewModel
import org.librefit.util.DataStoreManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    userPreferences: DataStoreManager,
    sharedViewModel: SharedViewModel,
    workoutId: Int,
    navController: NavHostController
){
    val keepWorkoutScreenOn = userPreferences.workoutScreenOn.collectAsState(initial = true).value

    val context = LocalContext.current

    if(keepWorkoutScreenOn){
        DisposableEffect(key1 = Unit) {
            val window = (context as Activity).window

            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            onDispose {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    val workoutWithExercises by sharedViewModel.getWorkoutWithExercises(workoutId).observeAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    workoutWithExercises?.workout?.let { Text(text = it.title) }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ){
        LazyColumn (modifier = Modifier.padding(it)){
            workoutWithExercises?.let { workout ->
                item { Text(text = "Exercises:") }
                items(workout.exercises){ exercise ->
                    Text(text = exercise.exerciseId)
                }
            }
        }
    }
}