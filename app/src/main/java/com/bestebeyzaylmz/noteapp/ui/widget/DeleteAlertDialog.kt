package com.bestebeyzaylmz.noteapp.ui.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    //genel alert dialog tanımı
    //silme, update gibi işlemlerde ortak yapı kullanılması için
    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Default.Warning, contentDescription = "Alert dialog icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmation.invoke()
                onDismissRequest.invoke()
            }) {
                Text(text = "Onayla")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Vazgeç")
            }
        },
        onDismissRequest = { onDismissRequest() }
    )
}