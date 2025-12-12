package com.example.guitarstore

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CategoryEntity::class, ProductEntity::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    abstract fun productDao(): ProductDao
}
