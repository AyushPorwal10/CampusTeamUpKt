package com.example.new_campus_teamup.screens.homescreens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R



@Composable
fun ProjectCard(onClick: () -> Unit = {}) {

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
            containerColor = Color(0xFF3BDECE)
        )
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.97f)
                ), shape = RoundedCornerShape(26.dp), elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.browse_project),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(R.drawable.projectcard),
                    contentScale = ContentScale.Fit,
                    contentDescription = null,

                    modifier = Modifier
                        .aspectRatio(5f / 2.4f)
                        .weight(0.2f)
                )


                Column(modifier = Modifier.weight(0.2f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("Build experience.\nBuild a network.\nBuild a project.")
                    Spacer(modifier = Modifier.size(10.dp))
                    Card(

                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Text(
                            "Let's go !",
                            fontWeight = FontWeight.SemiBold,
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
        }
    }
}
