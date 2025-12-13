package com.example.guitarstore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ControlHub : ViewModel() {
        private val dao = DatabaseInstance.db.categoryDao()
        private val productsDao = DatabaseInstance.db.productDao()

        var currentParentId: Int? = null

        val categories = MutableLiveData<List<CategoryEntity>>()
        val products = MutableLiveData<List<ProductEntity>>()

        suspend fun loadCategories(parentId: Int?) {
            val items = if (parentId == null) dao.getRootCategories() else dao.getChildren(parentId)
            categories.postValue(items)
        }

        suspend fun loadProducts(parentIds: List<Int>) {
            if (parentIds.size < 3) return
            val items = productsDao.getProductsByCategory(parentIds[0], parentIds[1], parentIds[2])
            products.postValue(items)
        }

        suspend fun addToCart(productId: Int) {
            val product = productsDao.getProductByID(productId)
            CartManager.add(product)
        }

        suspend fun removeFromCart(productId: Int) {
            val product = productsDao.getProductByID(productId)
            CartManager.remove(product)
        }
    }
