package com.example.guitarstore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guitarstore.state.CategoryState
import com.example.guitarstore.state.RootCategoryState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



class ControlHub : ViewModel() {
        private val dao = DatabaseInstance.db.categoryDao()
        private val productsDao = DatabaseInstance.db.productDao()

        val categories = MutableLiveData<List<CategoryEntity>>()
        val products = MutableLiveData<List<ProductEntity>>()
        var categoryState: CategoryState = RootCategoryState()


    suspend fun loadCategories() {
        categoryState.loadCategories(this)
    }


    suspend fun loadProducts(parentIds: List<Int>) {
        if (parentIds.size < 3) return

        val items = withContext(Dispatchers.IO) {
            productsDao.getProductsByCategory(
                parentIds[0],
                parentIds[1],
                parentIds[2]
            )
        }
        products.postValue(items)
    }


    suspend fun addToCart(productId: Int) {
        val product = withContext(Dispatchers.IO) {
            productsDao.getProductByID(productId)
        }
        CartManager.add(product)
    }

    suspend fun removeFromCart(productId: Int) {
        val product = withContext(Dispatchers.IO) {
            productsDao.getProductByID(productId)
        }
        CartManager.remove(product)
    }


    suspend fun loadRootCategories() {
        val items = withContext(Dispatchers.IO) {
            dao.getRootCategories()
        }
        categories.postValue(items)
    }


    suspend fun loadSubCategories(parentId: Int) {
        val items = withContext(Dispatchers.IO) {
            dao.getSubCategories(parentId)
        }
        categories.postValue(items)
    }
}
