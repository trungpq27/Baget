package com.mobile.expenseapp.service.response

import com.mobile.expenseapp.service.request.SyncRequest

data class SyncResponse(
    val status: String,
    val data: SyncRequest,
)
