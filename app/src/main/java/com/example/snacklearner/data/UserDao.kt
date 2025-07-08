package com.example.snacklearner.data

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)
    @Query("SELECT * FROM users ORDER BY id DESC LIMIT 1")
    fun getLastLoggedInUser(): UserEntity?

}
