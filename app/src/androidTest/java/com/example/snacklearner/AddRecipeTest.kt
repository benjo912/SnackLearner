package com.example.snacklearner

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class AddRecipeTest {

    @Test
    fun emptyFieldsShowsFillAllFieldsToast() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        prefs.edit().putInt("user_id", 1234).apply()

        launchFragmentInContainer<AddRecipeFragment>(themeResId = R.style.Theme_SnackLearner)

        onView(withId(R.id.buttonSaveRecipe)).perform(click())
    }
}