package com.example.new_campus_teamup.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@Composable
fun RatingStars(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    unRatedStarIcon: Painter,
    ratedStarIcon: Painter,
    modifier: Modifier = Modifier,
    iconSize: Dp = 40.dp,
    itemSpacing: Dp = 8.dp,
    animOverlapDelay: Long = 200L,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioHighBouncy,
        stiffness = Spring.StiffnessLow
    )
) {

    var animatedRating by remember { mutableIntStateOf(rating) }
    val boxesScale = List(5) {
        remember { Animatable(if (rating <= it) 0.8f else 1f) }
    }
    var prevRating by remember { mutableIntStateOf(rating) }


    LaunchedEffect(rating) {
        val isDecreased = rating < prevRating
        val boxes = if (isDecreased) boxesScale.take(prevRating).drop(rating).reversed()
        else boxesScale.drop(prevRating).take(rating - prevRating)

        prevRating = rating
        boxes.forEachIndexed { index, animatable ->
            launch {
                delay(index * animOverlapDelay)
                animatedRating = if (isDecreased) animatedRating - 1
                else animatedRating + 1
                animatable.animateTo(
                    targetValue = if (isDecreased) 0.8f else 1f,
                    animationSpec = animationSpec
                )
            }
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        repeat(5) { index ->
            Icon(
                modifier = Modifier
                    .size(iconSize)
                    .scale(boxesScale[index].value)
                    .clickable {
                        onRatingChange(index + 1)
                    },
                painter = if (index < animatedRating) ratedStarIcon else unRatedStarIcon,
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}