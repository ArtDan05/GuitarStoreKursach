package com.example.guitarstore

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CategoryEntity::class, ProductEntity::class, OrdersEntity::class, OrderIsCompletedEntity::class, ItemDescriptionEntity::class], version = 9)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    abstract fun productDao(): ProductDao

    abstract fun ordersDao(): OrdersDao

    abstract fun orderIsCompletedDao(): OrdersIsCompletedDao

    abstract fun itemDescriptionDao(): ItemDescriptionDao
}
