package com.rygital.moneytracker.data.model.database

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.os.Parcelable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.*

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency ORDER BY id")
    fun getAll(): List<Currency>

    @Query("SELECT * FROM currency ORDER BY id")
    fun getAllRx(): Single<List<Currency>>

    @Query("SELECT * FROM currency WHERE label = :label")
    fun findByLabel(label: String) : Currency?

    @Query("SELECT * FROM currency WHERE id = :id")
    fun findById(id: Long) : Currency

    @Update
    fun updateCurrencies(currencies: List<Currency>)

    @Insert
    fun insertAll(currencies: Array<Currency>)
}

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category ORDER BY id")
    fun getAll(): List<Category>
    @Query("SELECT * FROM category ORDER BY id")
    fun getAllRx(): Single<List<Category>>

    @Query("SELECT * FROM category WHERE label = :label")
    fun findByLabel(label: String) : Category?

    @Query("SELECT * FROM category WHERE id = :id")
    fun findById(id: Long) : Category?

    @Insert(onConflict = REPLACE)
    fun insertAll(categories: Array<Category>)

}

@Dao
interface AccountDao {

    @Query("SELECT * FROM account ORDER BY id")
    fun getAll(): List<Account>
    @Query("SELECT * FROM account ORDER BY id")
    fun getAllRx(): Single<List<Account>>

    @Query("SELECT * FROM account WHERE label = :label")
    fun findByLabel(label: String) : Account?

    @Query("SELECT * FROM account WHERE id = :id")
    fun findById(id: Long) : Account?

    @Insert(onConflict = REPLACE)
    fun insertAll(accounts: Array<Account>)

    @Insert(onConflict = REPLACE)
    fun insert(account: Account)
}

@Dao
interface TransactionDao {

    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    fun getAll(): List<Transaction>

    @Insert(onConflict = REPLACE)
    fun insert(transaction: Transaction)
    @Insert(onConflict = REPLACE)
    fun insertAll(transactions: Array<Transaction>)

    @Update
    fun update(transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Query("DELETE FROM `transaction` WHERE id = :id")
    fun delete(id: Long)

    @Query("""
        SELECT tr.id, tr.type, tr.amount, cu.id AS currencyId, cu.label AS currencyLabel,
            cu.symbol AS currencySymbol, cu.rate_to_usd AS currencyRate,
            ca.id AS categoryId, ca.label AS categoryLabel, ca.color AS categoryColor,
            ac.id AS accountId, ac.label AS accountLabel, ac.icon AS accountIcon, tr.date
        FROM `transaction` AS tr
            JOIN currency AS cu ON tr.currency_id = cu.id
            JOIN category AS ca ON tr.category_id = ca.id
            JOIN account AS ac ON tr.account_id = ac.id""")
    fun getDetailedTransactions(): Flowable<List<DetailedTransaction>>

    @Query("""
        SELECT tr.id, tr.type, tr.amount, cu.id AS currencyId, cu.label AS currencyLabel,
            cu.symbol AS currencySymbol, cu.rate_to_usd AS currencyRate,
            ca.id AS categoryId, ca.label AS categoryLabel, ca.color AS categoryColor,
            ac.id AS accountId, ac.label AS accountLabel, ac.icon AS accountIcon, tr.date
        FROM `transaction` AS tr
            JOIN currency AS cu ON tr.currency_id = cu.id
            JOIN category AS ca ON tr.category_id = ca.id
            JOIN account AS ac ON tr.account_id = ac.id
        WHERE tr.account_id = :accountId
        ORDER BY tr.date DESC""")
    fun getDetailedTransactionsForAccount(accountId: Long): Flowable<List<DetailedTransaction>>

    @Query("""
        SELECT tr.id, tr.type, tr.amount, cu.id AS currencyId, cu.label AS currencyLabel,
            cu.symbol AS currencySymbol, cu.rate_to_usd AS currencyRate,
            ca.id AS categoryId, ca.label AS categoryLabel, ca.color AS categoryColor,
            ac.id AS accountId, ac.label AS accountLabel, ac.icon AS accountIcon, tr.date
        FROM `transaction` AS tr
            JOIN currency AS cu ON tr.currency_id = cu.id
            JOIN category AS ca ON tr.category_id = ca.id
            JOIN account AS ac ON tr.account_id = ac.id
        WHERE tr.date >= :fromDate""")
    fun getDetailedTransactions(fromDate: Date): Flowable<List<DetailedTransaction>>
}

@Parcelize
data class DetailedTransaction (
        var id: Long,
        var type: Int,
        var amount: BigDecimal,
        var currencyId: Long,
        var currencyLabel: String,
        var currencySymbol: Char,
        var currencyRate : Double,
        var categoryId: Long,
        var categoryLabel: Int,
        var categoryColor: Int,
        var accountId: Long,
        var accountLabel: String,
        var accountIcon: Int,
        var date: Date
): Parcelable

@Dao
interface PatternDao {

    @Insert(onConflict = REPLACE)
    fun insert(transaction: Pattern)

    @Query("""
        SELECT pa.id, pa.type, pa.amount, cu.id AS currencyId, cu.label AS currencyLabel,
            cu.symbol AS currencySymbol, cu.rate_to_usd AS currencyRate,
            ca.id AS categoryId, ca.label AS categoryLabel, ca.color AS categoryColor,
            ac.id AS accountId, ac.label AS accountLabel, ac.icon AS accountIcon, pa.date
        FROM pattern AS pa
            JOIN currency AS cu ON pa.currency_id = cu.id
            JOIN category AS ca ON pa.category_id = ca.id
            JOIN account AS ac ON pa.account_id = ac.id
        ORDER BY pa.date DESC""")
    fun getAll(): Flowable<List<DetailedTransaction>>

    @Query("DELETE FROM pattern WHERE id = :id")
    fun delete(id: Long)

}