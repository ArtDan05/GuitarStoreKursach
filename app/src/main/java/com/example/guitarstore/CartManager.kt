package com.example.guitarstore

import android.widget.BaseAdapter
import android.widget.ListView

object CartManager {
    val items = mutableListOf<ProductEntity>()

    fun add(item: ProductEntity) {
        items.add(item)
    }

    fun remove(item: ProductEntity) {
        items.remove(item)
    }
}