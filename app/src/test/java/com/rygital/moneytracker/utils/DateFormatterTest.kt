package com.rygital.moneytracker.utils

import junit.framework.Assert.assertTrue
import org.junit.Test
import java.util.*

class DateFormatterTest {

    @Test
    fun `Should be today text`() {
        assertTrue(formatToYesterdayOrToday(Date(),"today", "yesterday").startsWith("today"))
    }

    @Test
    fun `Should be yesterday text`() {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)
        assertTrue(formatToYesterdayOrToday(yesterday.time,"today", "yesterday").startsWith("yesterday"))
    }

    @Test
    fun `Should be simple date text`() {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -2)
        val yesterdayOrToday = formatToYesterdayOrToday(yesterday.time, "today", "yesterday")
        assertTrue(!yesterdayOrToday.startsWith("yesterday") && !yesterdayOrToday.startsWith("today"))
    }
}