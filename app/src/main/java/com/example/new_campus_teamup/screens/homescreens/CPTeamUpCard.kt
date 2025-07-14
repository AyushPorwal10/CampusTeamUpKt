package com.example.new_campus_teamup.screens.homescreens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R


@Composable
fun CPTeamUp(onClick: () -> Unit) {

    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f
    )
    Card(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false

                    }
                )
            }
            .fillMaxWidth()
            .padding(20.dp), shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(
            8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFbce3f6)
        )
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(modifier = Modifier.fillMaxWidth(0.6f)) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFCCC0FE),
                                shape = RoundedCornerShape(
                                    topStart = 22.dp,
                                    topEnd = 12.dp,
                                    bottomStart = 12.dp,
                                    bottomEnd = 22.dp
                                )
                            )
                            .padding(12.dp)
                            .size(80.dp),
                    ) {
                        Text(stringResource(R.string.form_a_team_message1) + "\n" + stringResource(R.string.form_a_team_message2) , fontWeight = FontWeight.Medium, modifier = Modifier.weight(0.7f).clickable {

                        })
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Text(
                                stringResource(R.string.lets_code_together),
                                color = Color.Black,
                                modifier = Modifier
                                    .padding(
                                        start = 12.dp,
                                        end = 12.dp,
                                        top = 4.dp,
                                        bottom = 4.dp
                                    )
                                    .clickable {
                                        onClick()
                                    }
                            )
                        }
                    }
                }


                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF6FF00)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(
                            stringResource(R.string.cp_teamup),
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(
                                    start = 12.dp,
                                    end = 12.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                )
                                .clickable {
                                    //
                                }
                        )
                    }
                    Image(
                        painter = painterResource(R.drawable.developer_visual),
                        contentScale = ContentScale.Fit,
                        contentDescription = null,

                        modifier = Modifier
                            .aspectRatio(3.5f / 2.4f)
                    )
                }
            }
        }
    }
}
