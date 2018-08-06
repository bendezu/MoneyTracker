package com.rygital.moneytracker

import android.content.Intent
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import com.rygital.moneytracker.ui.home.HomeActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val testRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java, true, true)

    @Test
    fun simple_app_navigation_test() {
        testRule.launchActivity(Intent())
        Espresso.onView(ViewMatchers.withId(R.id.settings)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.about)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.tvVersion)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.about)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.addTransaction)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btnSave)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.addTransaction)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @After
    fun tearDown() {
        testRule.finishActivity()
    }
}