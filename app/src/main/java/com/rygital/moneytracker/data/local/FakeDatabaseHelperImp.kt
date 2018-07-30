package com.rygital.moneytracker.data.local

import com.rygital.moneytracker.R
import com.rygital.moneytracker.data.model.*
import com.rygital.moneytracker.data.model.Currency
import io.reactivex.Observable
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

class FakeDatabaseHelperImp @Inject constructor(): DatabaseHelper {
    private var accounts: List<Account> = listOf(
            Account(0, "Cash"),
            Account(1, "Bank Card")
    )

    private val categories: List<Category> = listOf(Category(0, "Car", BigDecimal(400), R.color.azure),
            Category(1, "Food", BigDecimal("1000"), R.color.green),
            Category(2, "Clothes", BigDecimal("2000"), R.color.red),
            Category(3, "Other", BigDecimal("700"), R.color.violet)
    )

    private val transactions: MutableList<Transaction> = mutableListOf(
            Transaction(TransactionType.DEBIT, BigDecimal("1243.0"), Currency.USD, 0, 0, Calendar.getInstance().time),
            Transaction(TransactionType.DEBIT, BigDecimal("2000.0"), Currency.USD, 1, 0, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, BigDecimal("200.0"), Currency.USD, 0, 0, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, BigDecimal("200.0"), Currency.USD, 0, 1, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, BigDecimal("200.0"), Currency.USD, 0, 1, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, BigDecimal("200.0"), Currency.EUR, 1, 2, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, BigDecimal("550.0"), Currency.USD, 1, 2, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, BigDecimal("5150.0"), Currency.RUB, 1, 2, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, BigDecimal("100.0"), Currency.USD, 0, 3, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, BigDecimal("100.0"), Currency.USD, 0, 3, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, BigDecimal("100.0"), Currency.USD, 1, 1, Calendar.getInstance().time),
            Transaction(TransactionType.CREDIT, BigDecimal("100.0"), Currency.USD, 1, 0, Calendar.getInstance().time)
    )

    override fun getAccounts(): Observable<List<Account>> {
        return Observable.just(accounts)
    }

    override fun getCategories(): Observable<List<Category>> {
        return Observable.just(categories)
    }

    override fun getTransactions(): Observable<List<Transaction>> {
        return Observable.just(transactions)
    }

    override fun addAccount(newAccount: Account) {
        // temporary not implemented
    }

    override fun addCategory(newCategory: Category) {
        // temporary not implemented
    }

    override fun addTransaction(newTransaction: Transaction) {
        transactions.add(newTransaction)
    }
}