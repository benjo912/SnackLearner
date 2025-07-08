package com.example.snacklearner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val ingredients: String,
    val createdBy: String,
    val likes: Int = 0,
    val dislikes: Int = 0
)
