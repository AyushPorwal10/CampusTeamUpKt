package com.example.campus_teamup.myAnimation

import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.campus_teamup.R
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.White
import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator

object TextAnimation {
    @Composable
     fun AnimatedText() {
         val textColor = if(isSystemInDarkTheme()) White else Black
        val text = stringResource(id = R.string.welcomeGreeting)

        val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }

        val typingDelayInMs = 50L

        var substringText by remember {
            mutableStateOf("")
        }
        LaunchedEffect(text) {
            delay(500)
            breakIterator.text = StringCharacterIterator(text)

            var nextIndex = breakIterator.next()
            while (nextIndex != BreakIterator.DONE) {
                substringText = text.subSequence(0, nextIndex).toString()
                nextIndex = breakIterator.next()
                delay(typingDelayInMs)
            }
        }
        Text(substringText , color = textColor , fontSize = 18.sp)
    }

}