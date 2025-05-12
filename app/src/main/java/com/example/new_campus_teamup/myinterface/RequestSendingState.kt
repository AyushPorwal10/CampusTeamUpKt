package com.example.new_campus_teamup.myinterface

import androidx.annotation.Keep
import kotlinx.coroutines.flow.StateFlow

@Keep
interface RequestSendingState {
    val isRequestSending : StateFlow<Boolean>
}