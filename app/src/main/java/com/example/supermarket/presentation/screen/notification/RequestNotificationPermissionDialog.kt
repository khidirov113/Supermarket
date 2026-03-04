package com.example.supermarket.presentation.screen.notification

import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.supermarket.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog(
    modifier: Modifier = Modifier,
    openDialog: MutableState<Boolean>,
    permissionState: PermissionState,
) {

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                permissionState.launchPermissionRequest()
            },
            title = { Text("Notification Permission") },
            text = { Text("Please grant notification permission to continue") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    permissionState.launchPermissionRequest()
                }) {
                    Text(text = "OK")
                }
            }

        )
    }

}