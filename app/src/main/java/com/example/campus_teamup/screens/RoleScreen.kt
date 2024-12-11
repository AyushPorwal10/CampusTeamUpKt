package com.example.campus_teamup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.PrimaryBlack
import com.example.campus_teamup.myThemes.PrimaryWhiteGradient
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.Black
import com.example.campus_teamup.ui.theme.White

@Preview
@Composable
fun RolesScreen() {
    val textColor = if(isSystemInDarkTheme()) White else Black
    val bgColor = if(isSystemInDarkTheme()) PrimaryWhiteGradient else PrimaryBlack

    var queryText by remember { mutableStateOf("") }

    val active by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(bgColor)){


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalArrangement = Arrangement.Center ,
                horizontalAlignment = Alignment.CenterHorizontally ,
                ) {
                Row( verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceAround , modifier = Modifier.fillMaxWidth())
                {
                   OutlinedTextField(value = queryText, onValueChange = {searchQuery -> queryText = searchQuery} ,
                       colors = TextFieldStyle.myTextFieldColor() ,
                       shape = TextFieldStyle.defaultShape ,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.searchRoles),
                                style = MaterialTheme.typography.titleMedium,
                                color = textColor
                            )
                        },
                       leadingIcon = {
                           Icon(painter = painterResource(id = R.drawable.search), contentDescription = null , modifier = Modifier.size(22.dp))
                       })

                    // filer option
                    Icon(painter = painterResource(id = R.drawable.filter), contentDescription = null , modifier = Modifier.size(26.dp))

                }

                SingleRole()
            }

    }
}