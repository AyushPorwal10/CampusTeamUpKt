package com.example.new_campus_teamup.screens

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.new_campus_teamup.myactivities.CreatePost
import com.example.new_campus_teamup.ui.theme.BackGroundColor
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.White


enum class MultifloatingState {
    Expanded,
    Collapse
}

enum class Identifier{
    Role,
    Vacancy,
    Project
}
class MinFabItem(
    val icon : ImageBitmap,
    val label : String,
    val identifier : String
)



@Composable
fun MultipleFabButton(
    context : Context,
    multifloatingState: MultifloatingState,
    onMultifloatingStateChange: (MultifloatingState) -> Unit,
    items: List<MinFabItem>
) {
    val transition = updateTransition(targetState = multifloatingState, label = "transition")

    val rotate by transition.animateFloat(label = "rotate") {
        if (it == MultifloatingState.Expanded) 315f else 0f
    }
    val fabScale by transition.animateFloat(label = "FabScale"){
        if(it == MultifloatingState.Expanded) 30f else 0f
    }
    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(durationMillis = 50) }
    ) {
        if(it == MultifloatingState.Expanded) 1f else 0f
    }

    val textShadow by transition.animateDp(
        label = "textShadow",
        transitionSpec = { tween(durationMillis = 50) }
    ) {
        if(it == MultifloatingState.Expanded)  2.dp else 0.dp
    }


    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.background(Color.Transparent)//try to remove
    ){

        if(transition.currentState == MultifloatingState.Expanded){
            items.forEach {
                MinFab(item = it,
                    onMinFabItemClick = {minFabItem ->
                        when(minFabItem.identifier){

                            Identifier.Role.name ->{
                                val intent = Intent(context , CreatePost::class.java)
                                intent.putExtra("status","Role")
                                context.startActivity(intent)
                            }
                            Identifier.Vacancy.name ->{
                                val intent = Intent(context , CreatePost::class.java)
                                intent.putExtra("status","Vacancy")
                                context.startActivity(intent)
                            }
                            Identifier.Project.name ->{
                                val intent = Intent(context , CreatePost::class.java)
                                intent.putExtra("status","Project")
                                context.startActivity(intent)
                            }

                        }
                    },
                    alpha = alpha,
                    textShadow = textShadow,
                    fabScale = fabScale
                )
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
        FloatingActionButton(
            onClick = {
                onMultifloatingStateChange(
                    if (transition.currentState == MultifloatingState.Expanded) {
                        MultifloatingState.Collapse
                    } else {
                        MultifloatingState.Expanded
                    }
                )
            },
            modifier = Modifier.clip(RoundedCornerShape(30.dp)),
            containerColor = BorderColor
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.rotate(rotate)
            )
        }
    }
}



@Composable
fun MinFab(
    item: MinFabItem,
    alpha : Float,
    textShadow: Dp,
    fabScale : Float,
    shadowLabel : Boolean = true,
    onMinFabItemClick: (MinFabItem) -> Unit
) {
    val shadow = Color.Black.copy(0.5f)

    Row {


        if(shadowLabel){

            Text(
                text = item.label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                modifier = Modifier.
                clip(RoundedCornerShape(22.dp))
                    .alpha(
                        animateFloatAsState(
                            targetValue = alpha, animationSpec = tween(50)
                        ).value
                    )
                    .border(1.dp , BorderColor, RoundedCornerShape(22.dp))
                    .background(BackGroundColor) // Keep background
                    .padding(10.dp))
            Spacer(modifier = Modifier.size(16.dp))

        }

        Canvas(
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                    onClick = {
                        onMinFabItemClick.invoke(item)
                    }
                )
        ) {


            drawImage(
                image = item.icon,
                topLeft = Offset(
                    center.x - (item.icon.width / 2),
                    center.y - (item.icon.width / 2),
                ),
                alpha = alpha,
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
        }
    }
}
