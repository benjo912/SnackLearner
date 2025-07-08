package com.example.snacklearner

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RecipeDetailsActivity : AppCompatActivity() {

    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        val title = intent.getStringExtra("recipe_title") ?: ""
        val description = intent.getStringExtra("recipe_description") ?: ""
        val ingredients = intent.getStringExtra("recipe_ingredients") ?: ""

        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val ingredientsTextView: TextView = findViewById(R.id.ingredientsTextView)
        val saveButton: Button = findViewById(R.id.saveFavoriteButton)
        val deleteButton: Button = findViewById(R.id.deleteButton)
        val likesTextView: TextView = findViewById(R.id.likesTextView)
        val dislikesTextView: TextView = findViewById(R.id.dislikesTextView)
        val author = intent.getStringExtra("recipe_author")
        val authorTextView = findViewById<TextView>(R.id.textViewAuthor)
        authorTextView.text = "Autor: $author"

        titleTextView.text = title
        descriptionTextView.text = description
        ingredientsTextView.text = ingredients

        isAdmin = intent.getBooleanExtra("isAdmin", false)

        val sharedPref = getSharedPreferences("votes", Context.MODE_PRIVATE)
        val likes = sharedPref.getInt("$title-likes", 0)
        val dislikes = sharedPref.getInt("$title-dislikes", 0)

        likesTextView.text = "Lajkovi: $likes"
        dislikesTextView.text = "Dislajkovi: $dislikes"

        saveButton.setOnClickListener {
            val pref = getSharedPreferences("favorites", Context.MODE_PRIVATE).edit()
            pref.putString(title, "$description||$ingredients")
            pref.apply()
            Toast.makeText(this, "Recept spremljen u favorite", Toast.LENGTH_SHORT).show()
        }

        if (isAdmin) {
            deleteButton.visibility = Button.VISIBLE
            deleteButton.setOnClickListener {

                Toast.makeText(this, "Recept obrisan", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            deleteButton.visibility = Button.GONE
        }
    }
}
