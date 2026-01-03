package com.example.new_campus_teamup.yourposts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.helper.ToastHelper


@Composable
fun ShowPostedVacancy(yourPostViewModel: YourPostViewModel ) {

    val vacancyList = yourPostViewModel.postedVacancy.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        yourPostViewModel.deletePostEvent.collect { message->
            ToastHelper.showToast(context , message)
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(vacancyList.value){
            YourSingleVacancy(vacancy = it, onVacancyDelete = {
                yourPostViewModel.deleteVacancy(it.postId)
            })
        }
        item {
            if(vacancyList.value.isEmpty()){
                Box( contentAlignment = Alignment.Center) {
                    LoadAnimation(
                        modifier = Modifier.size(200.dp),
                        animation = R.raw.noresult,
                        playAnimation = true
                    )
                }
            }
        }


    }
}