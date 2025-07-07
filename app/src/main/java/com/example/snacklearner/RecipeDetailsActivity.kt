package com.example.snacklearner

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RecipeDetailsActivity : AppCompatActivity() {

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

        titleTextView.text = title
        descriptionTextView.text = description
        ingredientsTextView.text = ingredients

        saveButton.setOnClickListener {
            val sharedPref = getSharedPreferences("favorites", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(title, "$description||$ingredients")
            editor.apply()

            Toast.makeText(this, "Recept spremljen u favorite", Toast.LENGTH_SHORT).show()
        }
    }
}
