package org.librefit.ui.screens.workout

import android.app.Activity
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import org.librefit.R
import org.librefit.data.DataStoreManager
import org.librefit.data.ExerciseDC
import org.librefit.db.Set
import org.librefit.ui.components.ConfirmExitDialog
import org.librefit.ui.components.ExerciseDetailModalBottomSheet
import org.librefit.ui.screens.createRoutine.ExerciseWithSets
import org.librefit.ui.screens.createRoutine.SetMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    userPreferences: DataStoreManager,
    workoutId: Int,
    navController: NavHostController,
    list: List<ExerciseDC>
){
    val viewModel : WorkoutScreenViewModel = viewModel()

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

    

    var showExitDialog by remember { mutableStateOf(false) }
    
    BackHandler (enabled = !showExitDialog){
        showExitDialog = true
    }

    if(showExitDialog){
        ConfirmExitDialog(
            text = stringResource(id = R.string.label_exit_workout),
            onExit = {
                navController.popBackStack()
                showExitDialog = false
            },
            onDismiss = { showExitDialog = false }
        )
    }


    viewModel.getExercisesFromWorkout(workoutId)


    LaunchedEffect(viewModel.exercises) {
        viewModel.exercises.value.forEach { exercise ->
            val item = list.associateBy { it.id }[exercise.exerciseId]
            if (item != null) {
                viewModel.getSetsFromExercise(exercise.id)

                viewModel.addExerciseWithSets(
                    ExerciseWithSets(
                        exercise = item,
                        exerciseId = exercise.id,
                        note = exercise.notes!!
                    )
                )
            }
        }
    }

    val exercisesWithSets by viewModel.exercisesWithSets.collectAsState()

    var isModalSheetOpen by remember { mutableStateOf(false) }

    /**
     * Used to display information about the selected exercise in [ExerciseDetailModalBottomSheet]
     */
    var selectedExercise by remember { mutableStateOf<ExerciseDC?>(null) }

    val animatedProgress = animateFloatAsState(
        targetValue = viewModel.progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    ).value

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.label_workout)) },
                navigationIcon = {
                    IconButton(
                        onClick = { showExitDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.label_navigate_back)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar (
                modifier = Modifier.height(120.dp)
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth(),
                ){
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Stopwatch()
                }
            }
        }
    ){ paddingValues ->
        LazyColumn (
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 15.dp, end = 15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            items(exercisesWithSets, key = { it.id }) { exerciseWithSets ->
                ExerciseCard(
                    exerciseWithSets = exerciseWithSets,
                    onDetail = {
                        selectedExercise = exerciseWithSets.exercise
                        isModalSheetOpen = true
                    },
                    addCompletedSet = { completed ->
                        viewModel.addCompletedSet(completed)
                    },
                    addSet = {
                            viewModel.addSetToExercise(exerciseWithSets.id)
                    },
                    updateSet = { set, value, mode ->
                        if(SetMode.WEIGHT == mode) {
                            viewModel.updateSet(
                                exerciseId = exerciseWithSets.id,
                                set = set,
                                weight = value
                            )
                        } else if (SetMode.REPS == mode) {
                            viewModel.updateSet(
                                exerciseId = exerciseWithSets.id,
                                set = set,
                                reps = value ,
                            )
                        } else if(SetMode.TIME == mode){
                            /* TODO */
                        }
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(5.dp)) }
        }
    }

    if(isModalSheetOpen){
        ExerciseDetailModalBottomSheet(exercise = selectedExercise!!) { isModalSheetOpen = false }
    }
}

@Composable
private fun ExerciseCard(
    exerciseWithSets: ExerciseWithSets,
    onDetail: () -> Unit,
    addCompletedSet : (Boolean) -> Unit,
    addSet : () -> Unit,
    updateSet : (Set, Int, SetMode) -> Unit
){


    Log.d("ExerciseCard", "Recomposing ExerciseCard for exercise ID: ${exerciseWithSets.id}")


    ElevatedCard {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = exerciseWithSets.exercise.name,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onDetail() }) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = Icons.Default.Info.name)
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.label_notes))},
                value = exerciseWithSets.note,
                onValueChange = {
                    //exerciseWithSets?.note = it
                },
                readOnly = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider()

            //Headline set
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.label_exercise_card_set), color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.weight(1.2f))
                Text(text = stringResource(id = R.string.label_exercise_card_reps), color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.weight(1.3f))
                Text(text = stringResource(id = R.string.label_exercise_card_weight), color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    modifier = Modifier.weight(1f)
                )
            }

            //Sets
            exerciseWithSets.sets.forEachIndexed { i, set ->
                var checked by rememberSaveable { mutableStateOf(false) }

                var repValue by remember { mutableStateOf(if(set.reps != null) set.reps.toString() else "0") }
                var weightValue by remember { mutableStateOf(if(set.weight != null) set.weight.toString() else "0") }
                var repError by remember { mutableStateOf(false) }
                var weightError by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topEndPercent = if (i == 0) 20 else 0,
                                topStartPercent = if (i == 0) 20 else 0,
                                bottomEndPercent = if (i == exerciseWithSets.sets.size - 1) 20 else 0,
                                bottomStartPercent = if (i == exerciseWithSets.sets.size - 1) 20 else 0
                            )
                        )
                        .background(if (checked) MaterialTheme.colorScheme.inversePrimary.copy(0.3f) else Color.Transparent)
                        .height(80.dp)
                        .fillMaxWidth()
                        .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("${i + 1}", color = MaterialTheme.colorScheme.onSurface)

                    Spacer(modifier = Modifier.weight(2.5f))
                    //Reps
                    OutlinedTextField(
                        modifier = Modifier.width(80.dp),
                        value = repValue,
                        onValueChange = { string ->
                            if(string.all { it.isDigit() } ) {
                                if( string.length > 4 ){
                                    repError = true
                                } else {
                                    repError = false
                                    repValue = string
                                    updateSet(set, repValue.ifEmpty{"0"}.toInt(), SetMode.WEIGHT)
                                }
                            }
                        },
                        singleLine = true,
                        isError = repError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )

                    Spacer(modifier = Modifier.weight(1f))


                    //Weight
                    OutlinedTextField(
                        modifier = Modifier.width(100.dp),
                        value = weightValue,
                        suffix = { Text("kg")},
                        onValueChange = { string ->
                            if(string.all { it.isDigit() } ) {
                                if( string.length > 4){
                                    weightError = true
                                } else {
                                    weightValue = string
                                    weightError = false
                                    updateSet(set, weightValue.ifEmpty{"0"}.toInt(), SetMode.WEIGHT)
                                }
                            }
                        },
                        singleLine = true,
                        isError = weightError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Checkbox(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            addCompletedSet(it)
                        }
                    )

                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider()

            TextButton(
                onClick = {
                    addSet()
                },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null
                )
                Spacer( modifier = Modifier.weight(1f) )
                Text( text = stringResource(id = R.string.label_exercise_card_add) )
                Spacer( modifier = Modifier.weight(1.3f) )
            }

        }
    }
}

@Composable
private fun Stopwatch() {
    var elapsedTime by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(true) }
    var buttonSize by remember { mutableIntStateOf(80) }
    val animatedExpansion = animateIntAsState(
        targetValue = buttonSize,
        label = ""
    )

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000)
            elapsedTime++
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.width(80.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Elapsed time:", style = MaterialTheme.typography.bodySmall)
            Text(
                text = formatTime(elapsedTime),
            )
        }
        Box (modifier = Modifier.height(70.dp).width(100.dp), contentAlignment = Alignment.Center){
            FilledIconButton(
                onClick = {
                    isRunning = !isRunning
                    buttonSize = if (isRunning) 80 else 55
                },
                modifier = Modifier.size(animatedExpansion.value.dp)
            ) {
                Icon(
                    imageVector = if (isRunning) {
                        ImageVector.vectorResource(id = R.drawable.ic_pause)
                    } else Icons.Default.PlayArrow ,
                    contentDescription = stringResource(id = if (isRunning) R.string.label_pause else R.string.label_play),
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(80.dp))
    }
}

private fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format(Locale.current.platformLocale,"%02d:%02d:%02d", hours, minutes, secs)
}