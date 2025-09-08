package com.example.new_campus_teamup.saveditems

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.new_campus_teamup.R
import com.example.new_campus_teamup.helper.LoadAnimation
import com.example.new_campus_teamup.mydataclass.RoleDetails
import com.example.new_campus_teamup.roleprofile.screens.SingleRoleCard
import com.example.new_campus_teamup.ui.theme.BackGroundColor


@Composable
fun ShowSavedRoles(savedRoleList: State<List<RoleDetails>>, onRoleUnsave: (String) -> Unit, onReportRoleBtnClick : (String) -> Unit) {



    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(savedRoleList.value) {
            Log.d("FetchingRoles", "Single Role loads")
            SingleRoleCard(it, onSaveRoleClicked = {
                onRoleUnsave(it.roleId)
            } , onReportRoleBtnClick = {
                onReportRoleBtnClick(it.roleId)
            }, isSaved = true)
        }
        item {
            if(savedRoleList.value.isEmpty()){
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