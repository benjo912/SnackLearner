package com.example.snacklearner

import android.content.Context
import android.os.Bundle
import android.widget.Button
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
    private lateinit var likeButton: Button
    private lateinit var dislikeButton: Button
    private lateinit var likesTextView: TextView
    private lateinit var dislikesTextView: TextView

    private var likeCount = 0
    private var dislikeCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        recipeTitleTextView = findViewById(R.id.titleTextView)
        recipeDescriptionTextView = findViewById(R.id.descriptionTextView)
        recipeAuthorTextView = findViewById(R.id.recipeAuthorTextView)
        likeButton = findViewById(R.id.likeButton)
        dislikeButton = findViewById(R.id.dislikeButton)
        likesTextView = findViewById(R.id.likesTextView)
        dislikesTextView = findViewById(R.id.dislikesTextView)

        val recipeId = intent.getIntExtra("recipe_id", -1)

        if (recipeId != -1) {
            loadRecipe(recipeId)
        }

        likeButton.setOnClickListener {
            likeCount++
            likesTextView.text = likeCount.toString()

        }

        dislikeButton.setOnClickListener {
            dislikeCount++
            dislikesTextView.text = dislikeCount.toString()

        }
    }

    private fun loadRecipe(recipeId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@RecipeDetailsActivity)
            val recipe = db.recipeDao().getRecipeById(recipeId)

            recipe?.let {
                val author = db.userDao().getUserByUsername(it.createdBy)

                runOnUiThread {
                    recipeTitleTextView.text = it.title
                    recipeDescriptionTextView.text = it.description
                    recipeAuthorTextView.text = "Autor: ${author?.username ?: "Nepoznat"}"
                    likeCount = it.likes
                    dislikeCount = it.dislikes
                    likesTextView.text = likeCount.toString()
                    dislikesTextView.text = dislikeCount.toString()
                }
            }
        }
    }


    private fun getLoggedInUserId(): Int {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("user_id", -1)
    }
}
