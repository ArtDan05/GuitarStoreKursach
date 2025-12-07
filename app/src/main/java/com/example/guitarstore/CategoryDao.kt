package com.example.guitarstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE parentId IS NULL")
    suspend fun getRootCategories(): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE parentId = :parentId")
    suspend fun getChildren(parentId: Int): List<CategoryEntity>

    @Insert
    suspend fun insertAll(items: List<CategoryEntity>)

    @Query("SELECT COUNT(*) FROM categories")
    suspend fun getCount(): Int

}
