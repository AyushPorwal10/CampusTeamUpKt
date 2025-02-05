package com.example.campus_teamup.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillSection(modifier: Modifier = Modifier) {
    var listOfSkills by remember { mutableStateOf( mutableListOf<String>()) }

    var currentSkill by remember { mutableStateOf("") }

    ConstraintLayout(modifier = modifier) {
        val (skillBox, inputSkill, addSkill, saveSkill) = createRefs()
        Column(modifier = Modifier.constrainAs(skillBox) {}) {
            FlowRow {
                listOfSkills.forEachIndexed { index , skill ->
                    SkillChips(skill = skill) {
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
            onSkillChange = {currentSkill = it})




        OutlinedButton(
            onClick = {listOfSkills = listOfSkills.toMutableList().apply {
                add(currentSkill)
            }},

            modifier = Modifier.constrainAs(addSkill){
                top.linkTo(inputSkill.bottom , margin = 10.dp)
            }
        ) {
            Text(text = "Add", color = White)
        }


        OutlinedButton(
            onClick = { },
            modifier = Modifier.constrainAs(saveSkill){
                top.linkTo(inputSkill.bottom , margin = 10.dp)
            }
        ) {
            Text(text = "Save", color = White)
        }
        createHorizontalChain(addSkill, saveSkill , chainStyle = ChainStyle.Spread)
    }
}


@Composable
fun InputSkill(modifier : Modifier, skill : String, onSkillChange : (String)->Unit) {


    OutlinedTextField(value = skill
        , onValueChange = onSkillChange ,
        maxLines = 1,
        modifier = modifier.fillMaxWidth(0.8f),
        placeholder = { Text(text = "Enter Skill") },
        colors = TextFieldStyle.myTextFieldColor(),
        shape = TextFieldStyle.defaultShape)
}


@Composable
fun SkillChips(skill : String , removeSkill :()->Unit) {
    AssistChip(onClick = { /*TODO*/ }, label = { Text(text = skill , color = White) } ,

        trailingIcon = {
            Icon(imageVector = Icons.Filled.Clear,
                contentDescription ="" ,
                modifier = Modifier.clickable {
                    removeSkill()
                } , tint = White)
        },colors = AssistChipDefaults.assistChipColors(
            containerColor = BorderColor,
            labelColor = White,
        ), modifier = Modifier.padding(horizontal = 4.dp , vertical = 2.dp))
}
