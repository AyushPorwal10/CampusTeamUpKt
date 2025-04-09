package com.example.campus_teamup.helper

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.campus_teamup.R
import com.example.campus_teamup.myinterface.RequestSendingState
import com.example.campus_teamup.ui.theme.BackGroundColor
import com.example.campus_teamup.ui.theme.BorderColor
import com.example.campus_teamup.ui.theme.White


@Composable
fun  <T> ShowRequestDialog(viewModel: T , onCancel : () -> Unit, onConfirm : () -> Unit ) where T : ViewModel , T : RequestSendingState{


    val isRequestSending = viewModel.isRequestSending.collectAsState()
    Log.d("RequestSending","${isRequestSending.value}")
    AlertDialog(
        onDismissRequest = {
        },
        confirmButton = {

            if(isRequestSending.value){
                ProgressIndicator.showProgressBar(modifier = Modifier.size(20.dp), canShow = isRequestSending.value)
            }
            else{
                Button(onClick = {
                    onConfirm()
                } , colors = ButtonDefaults.buttonColors(
                    containerColor = BackGroundColor
                )) {
                    Text(text = "Confirm" , color = White , style = MaterialTheme.typography.titleSmall)
                }
            }


        },
        dismissButton = {
            if(!isRequestSending.value){
                Button(onClick = {
                    onCancel()
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = BackGroundColor
                ) ) {
                    Text(text = "Cancel", color = White , style = MaterialTheme.typography.titleSmall)
                }
            }

        },
        title = {
            Text(text = stringResource(id = R.string.send_request) ,  color = White , style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Text(text = stringResource(id = R.string.send_request_message) ,  color = White , style = MaterialTheme.typography.titleMedium)
        } , containerColor = BackGroundColor , modifier = Modifier
            .border(1.dp, BorderColor, RoundedCornerShape(30.dp))
    )
}
