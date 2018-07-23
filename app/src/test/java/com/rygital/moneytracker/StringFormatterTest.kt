package com.rygital.moneytracker

import com.rygital.moneytracker.utils.formatMoney
import junit.framework.Assert.assertEquals
import org.junit.Test

class StringFormatterTest {

    @Test
    fun testFormatMoney() {
        assertEquals("15.30", formatMoney(15.301234))
        assertEquals("15.30", formatMoney(15.3))
        assertEquals("15.32", formatMoney(15.319))
    }
}