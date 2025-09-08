package com.example.new_campus_teamup.helper

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


@Composable
fun HandleNotificationPermission(
    onGranted: () -> Unit = {},
    onDenied: () -> Unit = {}
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return // No need for permission

    val context = LocalContext.current
    var showRationale by remember { mutableStateOf(false) }
    var askedAlready by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onGranted()
        } else {
            onDenied()
            Toast.makeText(context, "Campus TeamUp can't send notification.", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!askedAlready) {
            askedAlready = true
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    onGranted()
                }

                shouldShowRequestPermissionRationale(context as? ComponentActivity, Manifest.permission.POST_NOTIFICATIONS) -> {
                    showRationale = true
                }

                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    if (showRationale) {
        NotificationRationaleDialog(
            onConfirm = {
                showRationale = false
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            onDismiss = {
                showRationale = false
                onDenied()
            }
        )
    }
}

fun shouldShowRequestPermissionRationale(activity: androidx.activity.ComponentActivity?, permission: String): Boolean {
    return activity?.shouldShowRequestPermissionRationale(permission) == true
}



@Composable
fun NotificationRationaleDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Notification Permission Needed") },
        text = { Text("We use notifications to keep you updated. Please allow them in the next prompt.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No Thanks")
            }
        }
    )
}