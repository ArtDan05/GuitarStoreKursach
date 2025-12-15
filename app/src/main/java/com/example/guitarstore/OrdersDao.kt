package com.example.guitarstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrdersDao {
    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getCount(): Int

    @Query("SELECT itemId FROM orders WHERE orderId = :id")
    suspend fun getOrderById(id: Int) : List<Int>

    @Insert
    suspend fun insertProduct(item: OrdersEntity)

    @Query("SELECT itemId FROM orders")
    suspend fun getAllProducts() : List<Int>

    @Query("SELECT orderId FROM orders ORDER BY orderId DESC LIMIT 1")
    suspend fun getLastOrderId() : Int

    @Query("DELETE FROM orders WHERE itemId = :itemId AND orderId = :orderId")
    suspend fun deleteItem(itemId: Int, orderId: Int)
}