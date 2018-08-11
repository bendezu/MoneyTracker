package com.rygital.moneytracker.ui.home

import com.rygital.moneytracker.data.model.database.DetailedTransaction

interface OnMenuClickListener {
    fun openAboutScreen()
    fun openSettingsScreen()
    fun openAddTransactionScreen(accountId: Int? = null, transaction: DetailedTransaction? = null)
    fun openAccountScreen(accountId: Int)
    fun openAddAccountScreen()
    fun navigateBack()
}