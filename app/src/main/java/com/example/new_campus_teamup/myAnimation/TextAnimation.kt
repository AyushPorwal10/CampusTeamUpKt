package com.example.new_campus_teamup.myAnimation

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.ui.theme.White
import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator

object TextAnimation {
    @Composable
     fun AnimatedText(modifier : Modifier) {
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
        Text(substringText , color = White , style = MaterialTheme.typography.titleMedium , modifier = modifier)
    }

}