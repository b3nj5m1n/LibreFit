package org.librefit.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmExitDialog(
    text : String,
    onExit : () -> Unit,
    onDismiss : () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss ,
        confirmButton = {
            TextButton(
                onClick = onExit
            ){
                Text(text = "Exit") /*TODO*/
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss ){
                Text(text = "Cancel")/*TODO*/
            }
        },
        text = { Text(text = text ) }
    )
}