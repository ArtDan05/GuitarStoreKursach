package com.example.guitarstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE fCh = :fCh AND sCh = :sCh AND tCh = :tCh")
    suspend fun getProductsByCategory(fCh: Int, sCh: Int, tCh: Int) : List<ProductEntity>

    @Insert
    suspend fun insertAll(items: List<ProductEntity>)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getCount(): Int

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductByID(id: Int) : ProductEntity
}
