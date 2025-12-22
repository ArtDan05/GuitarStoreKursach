package com.example.guitarstore.state

import com.example.guitarstore.ControlHub

interface CategoryState {
    suspend fun loadCategories(controlHub: ControlHub)
}