package com.example.new_campus_teamup.screens.profilescreens

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.myThemes.TextFieldStyle

@Composable
fun DeleteAccountDialog(
    isLoading: Boolean = false,
    onDismiss: () -> Unit = {},
    onConfirm: (String) -> Unit = {}
) {
    Dialog(
        onDismissRequest = {  },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        DeleteAccountDialogContent(
            isLoading = isLoading,
            onDismiss = onDismiss,
            // Simplified lambda passing
            onConfirm = onConfirm
        )
    }
}

@Composable
private fun DeleteAccountDialogContent(
    isLoading: Boolean = false,
    onDismiss: () -> Unit = {},
    onConfirm: (String) -> Unit = {}
) {
    var userEnteredCaptcha by remember { mutableStateOf("") }
    var generatedCaptcha by remember { mutableStateOf(generateCaptcha()) }

    val context = LocalContext.current

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(300), label = "alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .graphicsLayer(alpha = alpha)
            .padding(16.dp), // Overall padding can be here
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Delete Account",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Are you sure you want to delete your account? This action cannot be undone.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )

            Box(
                modifier = Modifier
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = generatedCaptcha,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            EnterCaptchaString(
                value = userEnteredCaptcha,
                onValueChange = { userEnteredCaptcha = it },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    enabled = !isLoading
                ) {
                    Text(
                        text = "Cancel",
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = {
                        if (userEnteredCaptcha == generatedCaptcha) {
                            onConfirm(userEnteredCaptcha)
                        } else {
                            Toast.makeText(context, "Invalid CAPTCHA", Toast.LENGTH_SHORT).show()
                            generatedCaptcha = generateCaptcha()
                            userEnteredCaptcha = ""
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color.Red
                    ),
                    enabled = !isLoading && userEnteredCaptcha.length == generatedCaptcha.length
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onError,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Text(
                            text = "Delete",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun EnterCaptchaString(
    modifier: Modifier = Modifier,
    value: String, // Hoisted State: Receive value
    onValueChange: (String) -> Unit // Hoisted State: Receive lambda to update value
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
         colors = TextFieldStyle.myTextFieldColor(),
         shape = TextFieldStyle.defaultShape,
        singleLine = true,
        label = {

            Text(text = "Enter CAPTCHA")
        },
        leadingIcon = {

            Icon(
                painterResource(id = R.drawable.captcha), contentDescription = "Captcha Icon",
                modifier = Modifier.size(22.dp), tint = Color(0xFF667eea)
            )

        },
        modifier = modifier
    )
}

fun generateCaptcha(length: Int = 6): String {
    val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { chars.random() }
        .joinToString("")
}
