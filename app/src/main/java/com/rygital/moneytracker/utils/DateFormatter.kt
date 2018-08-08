package com.rygital.moneytracker.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatToYesterdayOrToday(date: Date, todayString: String, yesterdayString:String): String {
    val calendar = Calendar.getInstance()
    calendar.time = date
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DATE, -1)
    val timeFormatter = SimpleDateFormat("HH:mm")

    return if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
        "$todayString " + timeFormatter.format(date)
    } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
        "$yesterdayString " + timeFormatter.format(date)
    } else {
        SimpleDateFormat("dd.MM.yy HH:mm").format(date)
    }
}