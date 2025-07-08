package com.example.snacklearner

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snacklearner.data.AppDatabase
import com.example.snacklearner.data.RecipeEntity
import kotlinx.coroutines.launch

class SavedRecipesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedRecipesAdapter
    private val savedRecipes = mutableListOf<RecipeEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.saved_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.savedRecipesRecyclerView)
        adapter = SavedRecipesAdapter(savedRecipes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val sharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val recipes = db.recipeDao().getRecipesByUserId(userId)
            savedRecipes.clear()
            savedRecipes.addAll(recipes)
            adapter.notifyDataSetChanged()
        }
    }

    private class SavedRecipesAdapter(private val recipes: List<RecipeEntity>) :
        RecyclerView.Adapter<SavedRecipesAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameTextView: TextView = view.findViewById(R.id.recipeName)
            val descriptionTextView: TextView = view.findViewById(R.id.recipeDescription)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.saved_recipes, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipe = recipes[position]
            holder.nameTextView.text = recipe.title
            holder.descriptionTextView.text = recipe.description
        }

        override fun getItemCount(): Int = recipes.size
    }
}
