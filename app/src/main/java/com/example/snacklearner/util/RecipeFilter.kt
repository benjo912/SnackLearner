package com.example.snacklearner.util

import com.example.snacklearner.model.Recipe
class RecipeFilter {

     fun filterRecipes(query: String, recipes: List<Recipe> ): List<Recipe> {
        return if (query.isEmpty()) {
            recipes
        } else {
            recipes.filter { recipe ->
                recipe.title.contains(query, ignoreCase = true) ||
                        recipe.description.contains(query, ignoreCase = true) ||
                        recipe.ingredients.contains(query, ignoreCase = true)
            }
        }
    }
}