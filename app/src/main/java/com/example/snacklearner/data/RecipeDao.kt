package com.example.snacklearner.data

import androidx.room.*

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: RecipeEntity)

    @Query("SELECT * FROM recipes WHERE userId = :userId")
    suspend fun getRecipesByUserId(userId: Int): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: Int): RecipeEntity?

    @Query("UPDATE recipes SET likes = likes + 1 WHERE id = :recipeId")
    suspend fun likeRecipe(recipeId: Int)

    @Query("UPDATE recipes SET dislikes = dislikes + 1 WHERE id = :recipeId")
    suspend fun dislikeRecipe(recipeId: Int)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)
    @Query("SELECT * FROM recipes")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomRecipe(): RecipeEntity?

}
