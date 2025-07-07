package com.example.snacklearner

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SavedRecipesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private var savedRecipes: List<Recipe> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.savedRecipesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        savedRecipes = loadSavedRecipes()
        recipeAdapter = RecipeAdapter(savedRecipes)
        recyclerView.adapter = recipeAdapter
    }

    private fun loadSavedRecipes(): List<Recipe> {
        val sharedPref = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        return sharedPref.all.map { entry ->
            val title = entry.key
            val value = entry.value as String
            val parts = value.split("||")
            val description = parts.getOrNull(0) ?: ""
            val ingredients = parts.getOrNull(1) ?: ""
            Recipe(title, description, ingredients)
        }
    }
}
