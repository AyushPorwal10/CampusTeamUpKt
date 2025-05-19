package com.example.new_campus_teamup.userprofile.screens

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
import com.example.new_campus_teamup.helper.ProgressIndicator
import com.example.new_campus_teamup.helper.ToastHelper
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.ui.theme.BorderColor
import com.example.new_campus_teamup.ui.theme.White
import com.example.new_campus_teamup.viewmodels.UserProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillSection(
    userProfileViewModel: UserProfileViewModel,
    modifier: Modifier
) {
    val tag = "Skills"
    var listOfSkills by remember { mutableStateOf<List<String>>(emptyList()) }

    val showProgressBar = remember { mutableStateOf(false) }

    val context = LocalContext.current

    var currentSkill by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val fetchedSkill = userProfileViewModel.skills.collectAsState()
    listOfSkills = fetchedSkill.value

    LaunchedEffect(Unit) {
        Log.d(tag , "Skills fetching process started")
        showProgressBar.value = true
        userProfileViewModel.fetchSkills()
        showProgressBar.value = false
        Log.d(tag , "Total Skills are : ${listOfSkills.size}")

    }
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (skillBox, progressBar , inputSkill, addSkill, saveSkill) = createRefs()

        Column(modifier = Modifier.constrainAs(skillBox) {}) {
            FlowRow {
                listOfSkills.forEachIndexed { index, skill ->
                    SkillChips(skill = skill) {
                        isEditing = true    // this means user want to edit skills
                        listOfSkills = listOfSkills.toMutableList().apply {
                            removeAt(index)
                        }
                    }
                }
            }
        }


        //  take skill as input

        InputSkill(modifier = Modifier
            .constrainAs(inputSkill) {
                top.linkTo(skillBox.bottom, margin = 10.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .fillMaxWidth(0.8f),
            skill = currentSkill,
            onSkillChange = { currentSkill = it })

        OutlinedButton(
            onClick = {
                isEditing = true
                if(currentSkill.trim().isEmpty()){
                    ToastHelper.showToast(context , "Skill can't be Empty")
                }
                else{
                    listOfSkills = listOfSkills.toMutableList().apply {
                        add(currentSkill)
                    }
                    currentSkill = "" // set empty after adding
                }

            },

            modifier = Modifier.constrainAs(addSkill) {
                top.linkTo(inputSkill.bottom, margin = 10.dp)
            }
        ) {
            Text(text = "Add", color = White)
        }


        OutlinedButton(
            onClick = {
                      isEditing = !isEditing
                if(!isEditing){
                    showProgressBar.value = true
                    coroutineScope.launch {
                        withContext(Dispatchers.IO){
                            userProfileViewModel.saveSkills(listOfSkills , onSuccess = {
                                ToastHelper.showToast(context , "Updated Successfully")
                            })
                        }
                        showProgressBar.value =false
                    }
                }
            },
            modifier = Modifier.constrainAs(saveSkill) {
                top.linkTo(inputSkill.bottom, margin = 10.dp)
            }
        ) {
            Text(text = if (isEditing) "Save" else "Edit", color = White)
        }
        createHorizontalChain(addSkill, saveSkill, chainStyle = ChainStyle.Spread)

        if(showProgressBar.value){
            ProgressIndicator.showProgressBar(
                modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(addSkill.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                true
            )
        }
    }
}


@Composable
fun InputSkill(modifier: Modifier, skill: String, onSkillChange: (String) -> Unit) {


    OutlinedTextField(
        value = skill, onValueChange = onSkillChange,
        maxLines = 1,
        modifier = modifier.fillMaxWidth(0.8f),
        placeholder = { Text(text = "Enter Skill") },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape
    )
}


@Composable
fun SkillChips(skill: String, removeSkill: () -> Unit) {
    AssistChip(onClick = { }, label = { Text(text = skill, color = White) },

        trailingIcon = {
            Icon(imageVector = Icons.Filled.Clear,
                contentDescription = "",
                modifier = Modifier.clickable {
                    removeSkill()
                }, tint = White
            )
        }, colors = AssistChipDefaults.assistChipColors(
            containerColor = BorderColor,
            labelColor = White,
        ), modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
    )
}
