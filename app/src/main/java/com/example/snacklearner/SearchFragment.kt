package com.example.snacklearner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snacklearner.data.RecipeDao
import com.example.snacklearner.model.Recipe
import com.example.snacklearner.util.RecipeFilter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeDao: RecipeDao
    private var recipeList: List<Recipe> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchEditText = view.findViewById(R.id.searchEditText)
        resultsRecyclerView = view.findViewById(R.id.resultsRecyclerView)

        resultsRecyclerView.layoutManager = LinearLayoutManager(context)
        recipeAdapter = RecipeAdapter(recipeList)
        resultsRecyclerView.adapter = recipeAdapter

        val app = requireActivity().application as SnackLearnerApp
        recipeDao = app.database.recipeDao()

        lifecycleScope.launch {
            recipeDao.getAll().collectLatest { recipesFromDb ->
                recipeList = recipesFromDb.map {
                    Recipe(it.title, it.description, it.ingredients)
                }
                recipeAdapter.recipes = recipeList
                recipeAdapter.notifyDataSetChanged()
            }
        }

        searchEditText.setOnEditorActionListener { _, _, _ ->
            performSearch()
            true
        }

        val toolbar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val drawerLayout = requireActivity().findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawerLayout)

        toolbar.setNavigationIcon(R.drawable.ic_menu_hamburger)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun performSearch(): Boolean {
        val query = searchEditText.text.toString()
        val recipeFilter = RecipeFilter()
        val filteredRecipes = recipeFilter.filterRecipes(query, recipeList)
        recipeAdapter.recipes = filteredRecipes
        recipeAdapter.notifyDataSetChanged()
        return true
    }

    private class RecipeAdapter(var recipes: List<Recipe>) :
        RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView = itemView.findViewById(android.R.id.text1)
            val descriptionTextView: TextView = itemView.findViewById(android.R.id.text2)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recipe = recipes[position]
            holder.titleTextView.text = recipe.title
            holder.descriptionTextView.text = recipe.description
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, RecipeDetailsActivity::class.java)
                intent.putExtra("recipe_title", recipe.title)
                intent.putExtra("recipe_description", recipe.description)
                intent.putExtra("recipe_ingredients", recipe.ingredients)
                context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int = recipes.size
    }
}
