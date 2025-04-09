package com.example.campus_teamup.myinterface

import kotlinx.coroutines.flow.StateFlow

interface RequestSendingState {
    val isRequestSending : StateFlow<Boolean>
}