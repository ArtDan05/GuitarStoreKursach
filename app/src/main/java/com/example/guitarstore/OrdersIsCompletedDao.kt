package com.example.guitarstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrdersIsCompletedDao {
    @Insert
    suspend fun insertOrderStatus(item: OrderIsCompletedEntity)

    @Query("UPDATE orderIsCompleted SET isCompleted = true WHERE orderId = :orderId")
    suspend fun checkAsCompleted(orderId: Int)

    @Query("SELECT isCompleted FROM orderIsCompleted WHERE orderId = :orderId")
    suspend fun getOrderStatus(orderId: Int) : Boolean
}