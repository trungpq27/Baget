package com.mobile.expenseapp.service.entity

import com.mobile.expenseapp.data.local.entity.AccountDto

data class AccountSync(
    val id: Int,
    val accountType: String,
    val balance: Double,
    val income: Double,
    val expense: Double,
){
    fun toDto(): AccountDto{
        return AccountDto(
            id,
            accountType,
            balance,
            income,
            expense,
        )
    }
}
fun AccountDto.toSync(): AccountSync{
    return AccountSync(
        id,
        accountType,
        balance,
        income,
        expense,
    )
}

