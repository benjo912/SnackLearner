package com.example.snacklearner

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var deleteButton: Button

    private var likeCount = 0
    private var dislikeCount = 0
    private var recipeId = -1

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
        deleteButton = findViewById(R.id.deleteButton)

        recipeId = intent.getIntExtra("recipe_id", -1)

        if (recipeId != -1) {
            loadRecipe(recipeId)
        }

        likeButton.setOnClickListener {
            val userId = getLoggedInUserId()
            if (userId != -1) {
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(this@RecipeDetailsActivity)
                    db.recipeDao().likeRecipe(recipeId)
                    likeCount++
                    runOnUiThread {
                        likesTextView.text = likeCount.toString()
                    }
                }
            } else {
                Toast.makeText(this, "Morate biti prijavljeni da biste lajkali!", Toast.LENGTH_SHORT).show()
            }
        }

        dislikeButton.setOnClickListener {
            val userId = getLoggedInUserId()
            if (userId != -1) {
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(this@RecipeDetailsActivity)
                    db.recipeDao().dislikeRecipe(recipeId)
                    dislikeCount++
                    runOnUiThread {
                        dislikesTextView.text = dislikeCount.toString()
                    }
                }
            } else {
                Toast.makeText(this, "Morate biti prijavljeni da biste dislajkali!", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            deleteRecipe(recipeId)
        }
    }

    private fun loadRecipe(recipeId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@RecipeDetailsActivity)
            val recipe = db.recipeDao().getRecipeById(recipeId)
            val currentUserId = getLoggedInUserId()

            recipe?.let {
                val author = db.userDao().getUserByUsername(it.createdBy)

                runOnUiThread {
                    recipeTitleTextView.text = it.title
                    recipeDescriptionTextView.text = it.description
                    val authorName = author?.username ?: "Nepoznat"
                    recipeAuthorTextView.text = getString(R.string.recipe_author, authorName)
                    likeCount = it.likes
                    dislikeCount = it.dislikes
                    likesTextView.text = likeCount.toString()
                    dislikesTextView.text = dislikeCount.toString()
                    deleteButton.visibility = if (author?.id == currentUserId) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun deleteRecipe(recipeId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@RecipeDetailsActivity)
            val recipe = db.recipeDao().getRecipeById(recipeId)
            recipe?.let {
                db.recipeDao().deleteRecipe(it)
                runOnUiThread {
                    Toast.makeText(this@RecipeDetailsActivity, "Recept obrisan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun getLoggedInUserId(): Int {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("user_id", -1)
    }
}

