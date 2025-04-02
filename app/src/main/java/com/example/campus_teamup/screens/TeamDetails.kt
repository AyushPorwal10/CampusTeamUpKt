package com.example.campus_teamup.screens


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campus_teamup.R
import com.example.campus_teamup.helper.CheckEmptyFields
import com.example.campus_teamup.helper.ToastHelper
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White
import com.example.campus_teamup.viewmodels.TeamDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsScreen(
    teamDetailsViewModel: TeamDetailsViewModel
) {
    val context = LocalContext.current
    var isEditing by remember {
        mutableStateOf(false)
    }
    var teamMembersUserName = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit){
        Log.d("TeamDetails", "Fetched")
        val list = teamDetailsViewModel.fetchTeamDetails()
        teamMembersUserName.clear()
        teamMembersUserName.addAll(list)
    }

    val bgColor = BackGroundColor
    val textColor = White
    val singleUserName = remember {
        mutableStateOf("")
    }
    val userId = teamDetailsViewModel.userId.collectAsState()
    Log.d("TeamDetailsUserId","Collected as state is ${userId.value}")

    val suggestionList by teamDetailsViewModel.listOfUserName.collectAsState()

    Log.d("SuggestionList", "$suggestionList")

    val searchingText = teamDetailsViewModel.searchUserNameText.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
    ) {
        val (listOfTeamMembers, addTeamMember, removeTeamMember, userNameInputField, editOrUpdate, divider, topAppBar, teamDetailsHeading) = createRefs()



        TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) },
            colors = topAppBarColors(
                containerColor = bgColor,
                titleContentColor = textColor,
                navigationIconContentColor = textColor
            ),
            navigationIcon = {
                IconButton(onClick = {

                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.browseback),
                        contentDescription = null,
                        tint = textColor
                    )
                }
            }, modifier = Modifier.constrainAs(topAppBar) {
                top.linkTo(parent.top)
            }
        )

        HorizontalDivider(modifier = Modifier
            .fillMaxWidth()
            .background(BorderColor)
            .constrainAs(divider) {
                top.linkTo(topAppBar.bottom)
            })

        Text(text = "Team Members", color = White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.constrainAs(teamDetailsHeading) {
                top.linkTo(divider.bottom, margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })



        LazyColumn(
            modifier = Modifier
                .padding(20.dp)
                .constrainAs(listOfTeamMembers) {
                    top.linkTo(teamDetailsHeading.bottom)
                }, verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(teamMembersUserName.size) { index ->
                AutoComplete(
                    userName = teamMembersUserName[index],
                    onDelete = {
                        teamMembersUserName.removeAt(index)
                    },
                    onUpdate = { it ->
                        teamMembersUserName[index] = it
                        teamDetailsViewModel.onSearchTextChange(it)
                    },
                    onUserNameSelect = {
                        teamMembersUserName[index] = it
                    },
                    index,
                    suggestionList,
                    teamMembersUserName
                )
            }
        }
        OutlinedButton(
            onClick = {
                Log.d("Add", "Add btn clicked")
                isEditing = !isEditing
                if (teamMembersUserName.size < 6)
                    teamMembersUserName.add("")
                else
                    ToastHelper.showToast(context, "You can add maximum 6 members")

            },
            modifier = Modifier.constrainAs(addTeamMember) {
                top.linkTo(listOfTeamMembers.bottom, margin = 16.dp)
            }) {
            Text(text = "Add", color = White)
        }
        OutlinedButton(
            onClick = {

                if (isEditing) {

                    Log.d("TeamDetails","Going to check empty username")
                    // checking empty fields
                    if (!CheckEmptyFields.checkTeamMemberUserNameIsEmpty(teamMembersUserName)) {

                        // checking if userId of current user is present or not

                        Log.d("TeamDetails","Going to check is username present")
                        Log.d("TeamDetails","User id is $userId")

                        if (CheckEmptyFields.isUserNameInPresent(teamMembersUserName, userId.value)) {//checking if current user in team or not

                            teamDetailsViewModel.checkIfUserInOtherTeam(
                                teamMembersUserName,
                                isPresent = { isUserNamePresent ->
                                    if (isUserNamePresent) {
                                        Log.d("TeamDetails","Member is not in any team")
                                        ToastHelper.showToast(context , "UserName is already present in other team")
                                    }
                                    else{
                                        teamDetailsViewModel.saveTeamDetails(teamMembersUserName)
                                        isEditing = false
                                    }
                                }) {

                            }
                        } else {
                            ToastHelper.showToast(context, "Your username must be there")
                        }
                    } else {
                        ToastHelper.showToast(context, "Username cannot be empty")
                    }



                }
            },
            modifier = Modifier.constrainAs(editOrUpdate) {
                top.linkTo(listOfTeamMembers.bottom, margin = 16.dp)
            }) {
            Text(text = if (isEditing) "Save" else "Edit", color = White)
        }

        createHorizontalChain(addTeamMember, editOrUpdate, chainStyle = ChainStyle.Spread)

    }
}

@Composable
fun AutoComplete(
    userName: String,
    onDelete: () -> Unit,
    onUpdate: (String) -> Unit,
    onUserNameSelect: (String) -> Unit,
    index: Int,
    suggestionList: List<String>,
    teamMembersUserName: SnapshotStateList<String>,

    ) {
    val context = LocalContext.current
    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded = false
                }
            )
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier
                    .background(BackGroundColor)
                    .fillMaxWidth(0.8f)
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
                shape = TextFieldStyle.defaultShape,
                value = userName,
                onValueChange = {
                        onUpdate(it)
                        expanded = true
                },
                placeholder = { Text(if (index == 0) "Team Leader UserName" else "Member Username") },
                colors = TextFieldStyle.myTextFieldColor(),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 12.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "arrow",
                            tint = Color.Black
                        )
                    }
                }
            )


            IconButton(onClick = {
                onDelete()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = null, tint = White, modifier = Modifier.size(20.dp)
                )

            }
        }

        AnimatedVisibility(visible = expanded) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .width(textFieldSize.width.dp),
                shape = RoundedCornerShape(10.dp)
            ) {

                LazyColumn(
                    modifier = Modifier.heightIn(max = 150.dp),
                ) {

                    if (userName.isNotEmpty()) {
                        items(suggestionList) {
                            ItemsCategory(title = it) { title ->
                                if (!teamMembersUserName.contains(title) ||title == userName) {
                                    onUserNameSelect(title)
                                    expanded = false
                                } else {
                                    ToastHelper.showToast(context, "Username already added")
                                }
                                expanded = false
                            }
                        }
                    }
                }

            }
        }


    }


}

@Composable
fun ItemsCategory(
    title: String,
    onSelect: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(10.dp)
    ) {
        Text(text = title, fontSize = 16.sp)
    }

}