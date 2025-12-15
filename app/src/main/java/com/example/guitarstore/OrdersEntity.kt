package com.example.guitarstore

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrdersEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderId: Int?,
    val itemId: Int?
)