package com.example.new_campus_teamup.screens.profilescreens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.myThemes.TextFieldStyle
import com.example.new_campus_teamup.ui.theme.IconColor
import com.example.new_campus_teamup.viewmodels.UserProfileViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsCard(userProfileViewModel: UserProfileViewModel) {

    var pressed by remember { mutableStateOf(false) }

    val userSkillsList = userProfileViewModel.skills.collectAsState()
    var showEditSkillDialog  = remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if(pressed) 0.97f else 1f
    )

    if(showEditSkillDialog.value){
        EditSkillsDialog(userSkillsList ,showEditSkillDialog.value , onDismiss = {
            showEditSkillDialog.value = false
        },userProfileViewModel)
    }

    Card(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false

                    }
                )
            }
            .fillMaxWidth(0.9f)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFbce3f6)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {


            DetailsCardHeading(true, onEditButtonClick = {
                showEditSkillDialog.value = true
            } , "Skills")


            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                userSkillsList.value.forEach { skill ->
                    SkillChip(skill, false)//false means don't need to show remove icon
                }
            }
        }
    }
}

@Composable
fun SkillChip(skill: String,showRemoveIcon : Boolean ,  onSkillRemove : (String) -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = skill,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            if(showRemoveIcon){
                Icon(painter = painterResource(R.drawable.rejectbtn), contentDescription = null , modifier = Modifier.padding(end = 6.dp)
                    .size(16.dp)
                    .clickable {
                        onSkillRemove(skill)
                    }, tint = Color.Black)
            }

        }

    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditSkillsDialog(
    userSkillsList: State<List<String>>,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    userProfileViewModel: UserProfileViewModel
) {

    val updatedSkillList = remember { mutableStateListOf<String>() }
    val currentSkillUserAdding = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(userSkillsList.value) {
        updatedSkillList.clear()
        updatedSkillList.addAll(userSkillsList.value)
    }
    if(showDialog){
        Dialog(onDismissRequest = {
            onDismiss()
        }) {


            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Color(0xFFbce3f6)) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                   DetailsCardHeading(
                       false , onEditButtonClick = {

                       } , "Skills")

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        updatedSkillList.forEach{
                            SkillChip(it, true , onSkillRemove = {it->
                                updatedSkillList.remove(it)
                            })
                        }
                    }

                    // here user can add skill
                    Row(horizontalArrangement = Arrangement.SpaceBetween , verticalAlignment = Alignment.CenterVertically){

                        OutlinedTextField(
                            value = currentSkillUserAdding.value,
                            onValueChange = {
                                currentSkillUserAdding.value = it
                            },
                            colors = TextFieldStyle.myTextFieldColor(),
                            shape = TextFieldStyle.defaultShape,
                            maxLines = 1,
                            label = {
                                Text(text = stringResource(id = R.string.enter_skill))
                            },
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.skills), contentDescription = null,
                                    modifier = Modifier.size(22.dp), tint = IconColor
                                )
                            },
                            modifier = Modifier.weight(0.8f)
                        )
//                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(onClick = {
                            if(currentSkillUserAdding.value.isNotEmpty() && !updatedSkillList.contains(currentSkillUserAdding.value)){
                                updatedSkillList.add(currentSkillUserAdding.value)
                                currentSkillUserAdding.value = ""// reset
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null , modifier = Modifier.size(22.dp), tint = IconColor)
                        }
                    }

                    Spacer(modifier = Modifier.size(20.dp))

                    CancelAndSaveButton(isLoading, onCancel = {
                        onDismiss()
                    }, onSave = {
                        userProfileViewModel.saveSkills(updatedSkillList , onSuccess = {
                            isLoading.value = false
                            onDismiss()
                        })
                    })
                }
            }
        }
    }
}


