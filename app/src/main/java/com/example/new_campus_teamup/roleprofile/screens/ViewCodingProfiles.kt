package com.example.new_campus_teamup.roleprofile.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.CheckEmptyFields
import com.example.new_campus_teamup.helper.ToastHelper

import com.example.new_campus_teamup.screens.profilescreens.CheckProfileLink
import com.example.new_campus_teamup.screens.profilescreens.DetailsCardHeading
import com.example.new_campus_teamup.viewmodels.ViewProfileViewModel

import com.example.new_campus_teamup.ui.theme.IconColor
import androidx.core.net.toUri


@Composable
fun ViewCodingProfiles(
    modifier: Modifier,
    viewProfileViewModel: ViewProfileViewModel
) {
    var pressed by remember { mutableStateOf(false) }
    val codingProfiles by viewProfileViewModel.codingProfilesDetails.collectAsState()

    val context = LocalContext.current



    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f
    )

    Card(
        modifier = modifier
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
            containerColor = Color(0xFFEDF9FE)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {


        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            DetailsCardHeading(false , onEditButtonClick = {

            } , "Coding Profiles")

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                if(codingProfiles.isEmpty()){
                    item {
                        ShowCodingProfiles("dummy",context , false)
                    }
                }
                else {
                    items(codingProfiles) { profileUrl ->
                        ShowCodingProfiles(profileUrl, context, true)
                    }
                }

            }

        }
    }
}


@Composable
fun ShowCodingProfiles(profileUrl: String, context : Context, showVisitButton : Boolean) {

    val platformNameAndIcon = CheckProfileLink.getPlatformNameAndIcon(profileUrl)
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(platformNameAndIcon.platformIcon),
                    tint = IconColor,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 6.dp).size(24.dp)
                )

                Text(
                    text = platformNameAndIcon.platformName,
                    fontWeight = FontWeight.Medium
                )
            }

            if(showVisitButton){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            color = Color(0xFF6E66D3),
                            shape = RoundedCornerShape(16.dp)
                        ).clickable {
                            if(CheckEmptyFields.isValidHttpsUrl(profileUrl)){
                                val browseLink = Intent(Intent.ACTION_VIEW , profileUrl.toUri())
                                context.startActivity(browseLink)
                            }
                            else {
                                ToastHelper.showToast(context , "No Profile found.")
                            }
                        }
                ) {
                    Text(
                        "Visit",
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp , vertical = 6.dp)
                    )

                }
            }


        }
    }


}


