package com.example.guitarstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDescriptionDao {
    @Insert
    suspend fun insertDescription(item: List<ItemDescriptionEntity>)

    @Query("SELECT description FROM itemDescription WHERE itemId = :id")
    suspend fun getDescriptionByItemID(id: Int) : String

    @Query("SELECT COUNT(*) FROM itemDescription")
    suspend fun getCount(): Int
}