package com.rygital.moneytracker.data.model.database

import android.arch.persistence.room.TypeConverter
import java.math.BigDecimal
import java.util.*

class DateConverter {

        @TypeConverter
        fun toDate(dateLong: Long) = Date(dateLong)

        @TypeConverter
        fun fromDate(date: Date) = date.time
}

class DecimalConverter {

        @TypeConverter
        fun fromString(value: String) = BigDecimal(value)

        @TypeConverter
        fun toString(bigDecimal: BigDecimal): String = bigDecimal.toPlainString()
}