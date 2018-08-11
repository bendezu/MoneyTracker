package com.rygital.moneytracker.utils

import com.rygital.moneytracker.R
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

fun getStartOfThisDay(): Date {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.clear(Calendar.MINUTE)
    cal.clear(Calendar.SECOND)
    cal.clear(Calendar.MILLISECOND)
    return cal.time
}

fun getStartOfThisWeek(): Date {
    val cal = Calendar.getInstance()
    cal.time = getStartOfThisDay()
    cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
    return cal.time
}

fun getStartOfThisMonth(): Date {
    val cal = Calendar.getInstance()
    cal.time = getStartOfThisDay()
    cal.set(Calendar.DAY_OF_MONTH, 1)
    return cal.time
}

fun getStartOfThisYear() : Date {
    val cal = Calendar.getInstance()
    cal.time = getStartOfThisMonth()
    cal.set(Calendar.MONTH, Calendar.JANUARY)
    return cal.time
}

fun getWelcomeMessage(): Int {
    val cal = Calendar.getInstance()
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5 until 12 -> R.string.good_morning
        in 12 until 16 -> R.string.good_day
        in 16 until 22 -> R.string.good_evening
        else -> R.string.good_night
    }
}