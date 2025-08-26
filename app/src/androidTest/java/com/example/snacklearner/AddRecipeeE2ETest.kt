package com.example.snacklearner

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddRecipeE2ETest {

    @Test
    fun addRecipeAndFindItViaSearch() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        prefs.edit().putInt("user_id", 5678).apply()


        launchFragmentInContainer<AddRecipeFragment>(themeResId = R.style.Theme_SnackLearner)
        onView(withId(R.id.editTextTitle)).perform(typeText("Chocolate Cake"), closeSoftKeyboard())
        onView(withId(R.id.editTextDescription)).perform(typeText("Delicious and moist"), closeSoftKeyboard())
        onView(withId(R.id.editTextIngredients)).perform(typeText("Chocolate, flour, eggs"), closeSoftKeyboard())
        onView(withId(R.id.buttonSaveRecipe)).perform(click())


        launchFragmentInContainer<SearchFragment>(themeResId = R.style.Theme_SnackLearner)
        onView(withId(R.id.searchEditText)).perform(typeText("Chocolate Cake"), pressImeActionButton())


        onView(withText("Chocolate Cake")).check(matches(isDisplayed()))
    }
}