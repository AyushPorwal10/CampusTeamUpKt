
package com.example.campus_teamup.myactivities

import android.content.Intent
import android.content.IntentSender.OnFinished
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.StatusBarColor
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.White
import kotlinx.coroutines.delay

class MySplash : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen {
                startActivity(Intent(this , Login::class.java))
                finish()
            }
        }
    }
}

@Preview
@Composable
fun SplashScreen(onSplashFinished: () -> Unit = {}) {

    val textColor = White
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.white_theme_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(10.dp))
                Text(text = stringResource(id = R.string.app_name) 
                    ,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium ,
                    color = textColor ,
                    maxLines = 1 ,)
        }

    }
    
    LaunchedEffect(Unit){
        delay(1000)
        onSplashFinished()
    }
}