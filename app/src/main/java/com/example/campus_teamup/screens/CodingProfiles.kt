package com.example.campus_teamup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.campus_teamup.R
import com.example.campus_teamup.myThemes.TextFieldStyle
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.White

@Composable
fun CodingProfiles() {

    var profiles by remember {
        mutableStateOf(mutableListOf<String>())
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (listOfProfiles , addProfilesBtn , editOrUpdateBtn) = createRefs()



        LazyColumn(modifier = Modifier.constrainAs(listOfProfiles){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        } , verticalArrangement = Arrangement.spacedBy(6.dp)){
            items(profiles.size){ index ->

                ProfileFields(
                    profileLink = profiles[index],
                    onDelete = {
                        profiles = profiles.toMutableList().apply {
                            removeAt(index)
                        }
                    },
                    onProfileChange = { newProfile ->
                        profiles = profiles.toMutableList().apply {
                            this[index] = newProfile
                        }
                    }
                )
            }


        }



        OutlinedButton(
            onClick = {
                profiles = profiles.toMutableList().apply {
                    add("")
                }
            },
            modifier = Modifier.constrainAs(addProfilesBtn) {
                top.linkTo(listOfProfiles.bottom, margin = 16.dp)

            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = BackGroundColor)
        ) {
            Text("Add " , color = White)
        }

        OutlinedButton(
            onClick = {  },
            modifier = Modifier.constrainAs(editOrUpdateBtn) {
                top.linkTo(listOfProfiles.bottom, margin = 16.dp)
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = BackGroundColor)
        ) {
            Text("Edit" , color = White)
        }

        createHorizontalChain(addProfilesBtn , editOrUpdateBtn , chainStyle = ChainStyle.Spread)

    }
}

@Composable
fun ProfileFields(
    profileLink : String ,
    onDelete : () -> Unit,
    onProfileChange : (String) -> Unit
) {



    Row(horizontalArrangement = Arrangement.SpaceEvenly , verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()){
        OutlinedTextField(
            value = profileLink,
            onValueChange = onProfileChange,
            modifier = Modifier
                .background(BackGroundColor),
            shape = TextFieldStyle.defaultShape,
            placeholder = { Text("Enter Profile URL (Leetcode ...") },
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
