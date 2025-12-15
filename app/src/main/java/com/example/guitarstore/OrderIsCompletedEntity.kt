package com.example.guitarstore

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orderIsCompleted")
data class OrderIsCompletedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderId: Int?,
    val isCompleted: Boolean?
)