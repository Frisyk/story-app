package com.dicoding.storyapp.view.hero

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.dicoding.storyapp.BuildConfig
import com.dicoding.storyapp.R
import com.dicoding.storyapp.view.main.MainActivity
import org.hamcrest.Matchers.anyOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLogoutLogin() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(anyOf(withId(R.id.logout), withText(R.string.logout))).perform(click())

        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.emailEditText)).perform(
            typeText(BuildConfig.EMAIL),
            pressImeActionButton()
        )
        onView(withId(R.id.passwordEditText)).perform(
            typeText(BuildConfig.PASSWORD),
            closeSoftKeyboard()
        )
        onView(withId(R.id.loginButton)).perform(click())
    }
}
