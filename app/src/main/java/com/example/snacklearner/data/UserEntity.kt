package com.example.snacklearner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val username: String,
    val password: String,
    val fullName: String,
    val email: String,
    val isAdmin: Boolean
)
