package com.rygital.moneytracker

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.rygital.moneytracker.data.model.database.FinanceDatabase
import com.rygital.moneytracker.data.model.database.Transaction
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.*

val transactions = arrayOf(
        Transaction(EXPENSE, BigDecimal(1), 1, 1, 0, Date(),1),
        Transaction(INCOME, BigDecimal(2), 1, 3, 0, Date(),2),
        Transaction(EXPENSE, BigDecimal(3), 1, 2, 1, Date(),3),
        Transaction(EXPENSE, BigDecimal(1), 1, 1, 1, Date(),4)
)

class DatabaseDaosTest {

    lateinit var database: FinanceDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), FinanceDatabase::class.java).build()
    }

    @Test
    fun currency_DAO_test() {
        database.currencyDao().insertAll(INITIAL_CURRENCIES)
        database.currencyDao().getAllRx().test().assertValue(INITIAL_CURRENCIES.asList())
    }

    @Test
    fun category_DAO_test() {
        database.categoryDao().insertAll(INITIAL_CATEGORIES)
        database.categoryDao().getAllRx().test().assertValue(INITIAL_CATEGORIES.asList())
    }

    @Test
    fun account_DAO_test() {
        database.accountDao().insertAll(INITIAL_ACCOUNTS)
        database.accountDao().getAllRx().test().assertValue(INITIAL_ACCOUNTS.asList())
    }

    @Test
    fun transaction_DAO_test() {
        database.currencyDao().insertAll(INITIAL_CURRENCIES)
        database.categoryDao().insertAll(INITIAL_CATEGORIES)
        database.accountDao().insertAll(INITIAL_ACCOUNTS)
        database.transactionDao().insertAll(transactions)

        val list = database.transactionDao().getAll()
        assertArrayEquals(transactions, list.toTypedArray())
    }
}