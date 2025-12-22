package com.example.guitarstore.state

import com.example.guitarstore.ControlHub

class SubCategoryState(private val parentCategoryId: Int) : CategoryState {
    override suspend fun loadCategories(controlHub: ControlHub) {
        controlHub.loadSubCategories(parentCategoryId)
    }
}