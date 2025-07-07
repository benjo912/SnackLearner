package com.example.snacklearner.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAll(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: RecipeEntity)

    @Query("UPDATE recipes SET likes = likes + 1 WHERE id = :id")
    suspend fun likeRecipe(id: Int)

    @Query("UPDATE recipes SET dislikes = dislikes + 1 WHERE id = :id")
    suspend fun dislikeRecipe(id: Int)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)
}
