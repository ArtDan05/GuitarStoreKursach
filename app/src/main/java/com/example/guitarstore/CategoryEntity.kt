package com.example.guitarstore

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val title: String,
    val parentId: Int?     // null — если корень
)
