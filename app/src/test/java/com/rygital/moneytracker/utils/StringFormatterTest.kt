package com.rygital.moneytracker.utils

import com.rygital.moneytracker.utils.formatMoney
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class StringFormatterTest {

    @Test
    fun testFormatMoney() {
        assertEquals("15,30", formatMoney(BigDecimal("15.301234")))
        assertEquals("15,30", formatMoney(BigDecimal("15.3")))
        assertEquals("15,32", formatMoney(BigDecimal("15.319")))
    }
}