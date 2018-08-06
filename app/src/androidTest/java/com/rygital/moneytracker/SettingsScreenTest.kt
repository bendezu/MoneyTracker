package com.rygital.moneytracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.click
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.rygital.moneytracker.ui.home.HomeActivity
import org.junit.After
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Before
import android.widget.TextView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.view.View
import org.hamcrest.Matcher
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito


@LargeTest
@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule
    val testRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java, true, false)

    @Before
    fun setUp() {
        testRule.launchActivity(Intent())
    }

    @Test
    fun should_save_settings_when_reenter() {

        onView(withId(R.id.settings)).perform(click())

        onView(withId(R.id.primaryCurrencySpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("EUR"))).perform(click())
        onView(withId(R.id.primaryCurrencySpinner)).check(matches(withSpinnerText(containsString("EUR"))))

        onView(withId(R.id.secondaryCurrencySpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("RUB"))).perform(click())
        onView(withId(R.id.secondaryCurrencySpinner)).check(matches(withSpinnerText(containsString("RUB"))))

        pressBack()

        onView(withId(R.id.settings)).perform(click())

        onView(withId(R.id.primaryCurrencySpinner)).check(matches(withSpinnerText(containsString("EUR"))))
        onView(withId(R.id.secondaryCurrencySpinner)).check(matches(withSpinnerText(containsString("RUB"))))


    }

    @After
    fun tearDown() {
        testRule.finishActivity()
    }

}