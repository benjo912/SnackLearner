package com.example.app
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun successfulLogin_showsSearchFragment() {
        onView(withId(R.id.usernameEditText))
            .perform(typeText("admin"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("admin"), closeSoftKeyboard())
        onView(withId(R.id.loginButton))
            .perform(click())
        onView(withId(R.id.toolbar))
            .check(matches(isDisplayed()))
    }
    @Test
    fun failedLogin_showsNotVisible() {
        onView(withId(R.id.usernameEditText))
            .perform(typeText("krivo"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("krivo"), closeSoftKeyboard())
        onView(withId(R.id.loginButton))
            .perform(click())
        onView(withId(R.id.toolbar))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}