package com.example.guitarstore

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CategoryEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}
