package com.rygital.moneytracker.data.model.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.rygital.moneytracker.INITIAL_ACCOUNTS
import com.rygital.moneytracker.INITIAL_CATEGORIES
import com.rygital.moneytracker.INITIAL_CURRENCIES
import com.rygital.moneytracker.INITIAL_TRANSACTIONS
import java.util.concurrent.Executors

@Database(entities = [(Currency::class), (Category::class), (Account::class), (Transaction::class)],
    version = 1)
@TypeConverters(DateConverter::class, DecimalConverter::class)
abstract class FinanceDatabase: RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        private var INSTANCE: FinanceDatabase? = null

        fun getInstance(context: Context): FinanceDatabase? {
            if (INSTANCE == null) {
                synchronized(Database::class) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE
        }

        private fun buildDatabase(context: Context) : FinanceDatabase? =
                Room.databaseBuilder(context.applicationContext,
                    FinanceDatabase::class.java, "finance.db")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadScheduledExecutor().execute {
                                val database = getInstance(context)
                                database?.currencyDao()?.insertAll(INITIAL_CURRENCIES)
                                database?.categoryDao()?.insertAll(INITIAL_CATEGORIES)
                                database?.accountDao()?.insertAll(INITIAL_ACCOUNTS)
                                database?.transactionDao()?.insertAll(INITIAL_TRANSACTIONS)
                            }
                        }
                    })
                    .build()

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}