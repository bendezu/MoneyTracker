package com.rygital.moneytracker.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class DimConverterTest {

    @Mock
    lateinit var context: Context
    @Mock
    lateinit var resources: Resources
    @Mock
    lateinit var displayMetrics: DisplayMetrics

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        displayMetrics.density = 1f
        `when`(resources.displayMetrics).thenReturn(displayMetrics)
        `when`(context.resources).thenReturn(resources)
    }

    @Test
    fun `Verify two way conversions`() {

        val testDp = 123f
        val px = dpToPx(context, testDp)
        val dp = pxToDp(context, px)

        assertEquals(testDp, dp, 0.1f)

    }
}