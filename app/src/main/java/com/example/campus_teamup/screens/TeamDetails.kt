package com.example.campus_teamup.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
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
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsScreen() {
    var teamMembersUserName by remember{ mutableStateOf(mutableListOf<String>()) }
    val bgColor = BackGroundColor
    val textColor = White

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(BackGroundColor)) {
        val (listOfTeamMembers , addTeamMember , removeTeamMember , editOrUpdate , divider ,topAppBar , teamDetailsHeading) = createRefs()



        TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                ),
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(painter = painterResource(id = R.drawable.browseback), contentDescription =null , tint = textColor )
                    }
                }, modifier = Modifier.constrainAs(topAppBar){
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
            modifier = Modifier.constrainAs(teamDetailsHeading){
                top.linkTo(divider.bottom , margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })



            LazyColumn(modifier = Modifier.padding(20.dp)
                .constrainAs(listOfTeamMembers) {
                                                top.linkTo(teamDetailsHeading.bottom)
                } , verticalArrangement = Arrangement.spacedBy(6.dp)){
                items(teamMembersUserName.size){ index ->
                    UserNameField(
                        userName  = teamMembersUserName[index],
                        onDelete = {
                            teamMembersUserName = teamMembersUserName.toMutableList().apply {
                                removeAt(index)
                            }
                        },
                        onUserNameChange = {newUserName ->
                         teamMembersUserName = teamMembersUserName.toMutableList().apply {
                             this[index] = newUserName
                         }
                        }
                    )
                }
            }



                OutlinedButton(
                    onClick = { teamMembersUserName = teamMembersUserName.toMutableList().apply {
                        add("")
                    } } ,
                    modifier = Modifier.constrainAs(addTeamMember){
                        top.linkTo(listOfTeamMembers.bottom , margin = 16.dp)
                    }) {
                    Text(text = "Add" , color = White)
                }


                OutlinedButton(
                    onClick = {  } ,
                    modifier = Modifier.constrainAs(editOrUpdate){
                        top.linkTo(listOfTeamMembers.bottom , margin = 16.dp)
                    }) {
                    Text(text = "Save" , color = White)
                }

                createHorizontalChain(addTeamMember , editOrUpdate , chainStyle = ChainStyle.Spread)
            }
}

@Composable
fun UserNameField(
    userName : String,
    onDelete : () -> Unit,
    onUserNameChange : (String) -> Unit
) {

    Row(horizontalArrangement = Arrangement.SpaceEvenly , verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            value = userName,
            onValueChange = onUserNameChange,
            modifier = Modifier
                .background(BackGroundColor)
                .fillMaxWidth(0.75f),
            shape = TextFieldStyle.defaultShape,
            placeholder = { Text("Team Member Username" , maxLines = 2) },
            colors = TextFieldStyle.myTextFieldColor(),
        )

        IconButton(onClick = {
            onDelete()
        }){
            Icon(
                painter = painterResource(id = R.drawable.password ),
                contentDescription = null , tint = White , modifier = Modifier.size(22.dp)
            )

        }
    }

}
