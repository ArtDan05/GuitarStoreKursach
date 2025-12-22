package com.example.guitarstore.state

import com.example.guitarstore.ControlHub

class RootCategoryState : CategoryState {
    override suspend fun loadCategories(controlHub: ControlHub) {
        controlHub.loadRootCategories()
    }
}