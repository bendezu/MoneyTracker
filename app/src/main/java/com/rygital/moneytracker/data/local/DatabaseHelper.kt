package com.rygital.moneytracker.data.local

import com.rygital.moneytracker.data.model.Account
import com.rygital.moneytracker.data.model.Category
import com.rygital.moneytracker.data.model.Transaction
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
interface DatabaseHelper {
    fun getAccounts(): Observable<List<Account>>
    fun getCategories(): Observable<List<Category>>
    fun getTransactions(): Observable<List<Transaction>>

    fun addAccount(newAccount: Account)
    fun addCategory(newCategory: Category)
    fun addTransaction(newTransaction: Transaction)
}