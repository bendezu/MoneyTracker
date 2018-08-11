package com.rygital.moneytracker.data.model.database

import android.arch.persistence.room.*
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import java.math.BigDecimal
import java.util.*


@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey var id: Long,
    @ColumnInfo(name = "label") var label: String,
    @ColumnInfo(name = "symbol") var symbol: Char,
    @ColumnInfo(name = "rate_to_usd") var rateToUsd: Double
)


@Entity(tableName = "category")
data class Category(
        @PrimaryKey var id: Long,
        @ColumnInfo(name = "label") @StringRes var label: Int,
        @ColumnInfo(name = "color") @ColorRes var color: Int
)


@Entity(tableName = "account")
data class Account(
    @ColumnInfo(name = "label") var label: String,
    @ColumnInfo(name = "icon") @DrawableRes var icon: Int,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) {
    override fun toString(): String {
        return label
    }
}


@Entity(tableName = "transaction", foreignKeys = arrayOf(
    ForeignKey(entity = Currency::class, parentColumns = arrayOf("id"), childColumns = arrayOf("currency_id")),
    ForeignKey(entity = Category::class, parentColumns = arrayOf("id"), childColumns = arrayOf("category_id")),
    ForeignKey(entity = Account::class, parentColumns = arrayOf("id"), childColumns = arrayOf("account_id"))
), indices = [Index("currency_id"), Index("category_id"), Index("account_id")])
data class Transaction(
        @ColumnInfo(name = "type") var type: Int,
        @ColumnInfo(name = "amount") var amount: BigDecimal,
        @ColumnInfo(name = "currency_id") var currencyId: Long,
        @ColumnInfo(name = "category_id") var categoryId: Long,
        @ColumnInfo(name = "account_id") var accountId: Long,
        @ColumnInfo(name = "date") var date: Date,
        @PrimaryKey(autoGenerate = true) var id: Long = 0
)


@Entity(tableName = "pattern", foreignKeys = arrayOf(
        ForeignKey(entity = Currency::class, parentColumns = arrayOf("id"), childColumns = arrayOf("currency_id")),
        ForeignKey(entity = Category::class, parentColumns = arrayOf("id"), childColumns = arrayOf("category_id")),
        ForeignKey(entity = Account::class, parentColumns = arrayOf("id"), childColumns = arrayOf("account_id"))
), indices = [Index("currency_id"), Index("category_id"), Index("account_id")])
data class Pattern (
        @ColumnInfo(name = "type") var type: Int,
        @ColumnInfo(name = "amount") var amount: BigDecimal,
        @ColumnInfo(name = "currency_id") var currencyId: Long,
        @ColumnInfo(name = "category_id") var categoryId: Long,
        @ColumnInfo(name = "account_id") var accountId: Long,
        @ColumnInfo(name = "date") var date: Date,
        @PrimaryKey(autoGenerate = true) var id: Long = 0
)