package com.example.campus_teamup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White

@Preview
@Composable
fun AskForEmailVerification(onDismiss : ()->Unit = {}) {


    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Box(modifier = Modifier.background(BackGroundColor).clip(RoundedCornerShape(22.dp)).padding(20.dp)) {
            ConstraintLayout {
                val (appLogo , message1 , message2) = createRefs()

                Image(painter = painterResource(id = R.drawable.email_sent),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(appLogo) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .size(50.dp))

                Text(text = "Click on verification link",
                    Modifier.constrainAs(message1){
                        top.linkTo(appLogo.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },color = White, style = MaterialTheme.typography.titleSmall)

                Text(text = "sent to your email",
                    Modifier.constrainAs(message2){
                        top.linkTo(message1.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },color = White, style = MaterialTheme.typography.titleSmall)
            }
        }

    }
}


