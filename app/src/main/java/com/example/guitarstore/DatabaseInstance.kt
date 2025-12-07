package com.example.guitarstore

import android.content.Context
import androidx.room.Room

object DatabaseInstance {
    lateinit var db: AppDatabase

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "guitars.db"
        ).fallbackToDestructiveMigration(true).build()
    }
}
