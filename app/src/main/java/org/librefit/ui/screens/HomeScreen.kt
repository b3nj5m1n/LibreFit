package org.librefit.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.librefit.R
import org.librefit.nav.Destination

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navController : NavHostController
){
    Column (
        modifier = Modifier
            .padding(paddingValues = innerPadding)
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxSize()
    ){
        Text(
            text = stringResource(id = R.string.label_quick_start),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall
        )

        //"Start empty workout" button
        TextButton(
            enabled = false,
            onClick = {
                //navController.navigate(Routes.WorkoutScreen)
            },
            colors = ButtonDefaults.buttonColors()
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = Icons.Default.PlayArrow.name
            )
            Spacer( modifier = Modifier.weight(1f) )
            Text( text = stringResource(id = R.string.label_start_empty_workout) )
            Spacer( modifier = Modifier.weight(1.3f) )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = stringResource(id = R.string.label_routine),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall
        )

        //"Create a workout routine" button
        TextButton(
            onClick = {
                navController.navigate(Destination.CreateRoutineScreen)
            },
            colors = ButtonDefaults.buttonColors()
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = Icons.Default.AddCircle.name
            )
            Spacer( modifier = Modifier.weight(1f) )
            Text(text = stringResource(id = R.string.label_create_routine) )
            Spacer( modifier = Modifier.weight(1.3f) )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(){
    val navController = rememberNavController()
    HomeScreen(innerPadding = PaddingValues(20.dp), navController)
}