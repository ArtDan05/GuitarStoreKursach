package com.example.guitarstore

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itemDescription")
data class ItemDescriptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemId: Int?,
    val description: String
)