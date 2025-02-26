package com.example.campus_teamup.roleprofile.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.helper.ProgressIndicator
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.roleprofile.ViewProfileViewModel
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.UserProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ViewSkills(
    modifier: Modifier,
    viewProfileViewModel: ViewProfileViewModel
) {

    val listOfSkills by viewProfileViewModel.skills.collectAsState()
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (skillBox) = createRefs()

        Column(modifier = Modifier.constrainAs(skillBox) {}) {
            FlowRow {
                listOfSkills.forEachIndexed { index, skill ->
                    SkillChips(skill = skill)
                }
            }
        }

    }
}
@Composable
fun SkillChips(skill: String) {
    AssistChip(onClick = { }, label = { Text(text = skill, color = White) },

        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = "",
                tint = White
            )
        }, colors = AssistChipDefaults.assistChipColors(
            containerColor = BorderColor,
            labelColor = White,
        ), modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
    )
}
