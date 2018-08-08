package com.rygital.moneytracker.ui.home

interface OnMenuClickListener {
    fun openAboutScreen()
    fun openSettingsScreen()
    fun openAddTransactionScreen(accountId: Int)
    fun openAccountScreen(accountId: Int)
    fun navigateBack()
}