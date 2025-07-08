package com.example.snacklearner

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.snacklearner.data.AppDatabase
import com.example.snacklearner.data.RecipeEntity
import kotlinx.coroutines.launch

class AddRecipeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val titleEditText = view.findViewById<EditText>(R.id.editTextTitle)
        val descriptionEditText = view.findViewById<EditText>(R.id.editTextDescription)
        val ingredientsEditText = view.findViewById<EditText>(R.id.editTextIngredients)
        val saveButton = view.findViewById<Button>(R.id.buttonSaveRecipe)

        val sharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            val ingredients = ingredientsEditText.text.toString().trim()

            if (title.isNotEmpty() && description.isNotEmpty() && ingredients.isNotEmpty()) {
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(requireContext())
                    val recipe = RecipeEntity(
                        title = title,
                        description = description,
                        ingredients = ingredients,
                        userId = userId
                    )
                    db.recipeDao().insertRecipe(recipe)
                    Toast.makeText(context, "Recept spremljen", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            } else {
                Toast.makeText(context, "Popunite sva polja", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
