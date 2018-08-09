package com.rygital.moneytracker.ui.transaction

import com.rygital.moneytracker.data.model.database.Account

class AddTransactionViewState(val accounts: List<Account>,
                              val categories: List<String>,
                              val currencies: List<String>,
                              val initialCurrency: Int)