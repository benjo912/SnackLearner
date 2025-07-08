package com.example.snacklearner

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.snacklearner.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeDetailsActivity : AppCompatActivity() {

    private lateinit var recipeTitleTextView: TextView
    private lateinit var recipeDescriptionTextView: TextView
    private lateinit var recipeAuthorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        recipeTitleTextView = findViewById(R.id.recipeTitleTextView)
        recipeDescriptionTextView = findViewById(R.id.recipeDescriptionTextView)
        recipeAuthorTextView = findViewById(R.id.recipeAuthorTextView)

        val recipeId = intent.getIntExtra("recipe_id", -1)

        if (recipeId != -1) {
            loadRecipe(recipeId)
        }
    }

    private fun loadRecipe(recipeId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@RecipeDetailsActivity)
            val recipe = db.recipeDao().getRecipeById(recipeId)

            recipe?.let {
                val author = db.userDao().getUserById(it.userId)

                runOnUiThread {
                    recipeTitleTextView.text = it.title
                    recipeDescriptionTextView.text = it.description
                    recipeAuthorTextView.text = "Autor: ${author?.username ?: "Nepoznat"}"
                }
            }
        }
    }
}
