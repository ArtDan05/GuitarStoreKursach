package com.example.guitarstore

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var categoryList: ListView
    private val dao get() = DatabaseInstance.db.categoryDao()

    private var currentParentId: Int? = null
    private val parentStack = mutableListOf<Int?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DatabaseInstance.init(this)
        categoryList = findViewById(R.id.CategoryList)

        lifecycleScope.launch {
            preloadDataIfNeeded()
            loadCategories(null)
        }

        categoryList.setOnItemClickListener { adapter, view, position, id ->
            val item = adapter.getItemAtPosition(position) as CategoryEntity

            if (item.title == "<- Назад") {
                navigateBack()
            } else {
                openCategory(item.id)
            }
        }
    }
    private suspend fun preloadDataIfNeeded() {
        val count = dao.getCount()
        if (count > 0) return

        val data = listOf(
            CategoryEntity(id = 1, title = "Гитары", parentId = null),
            CategoryEntity(id = 2, title = "Бас", parentId = null),
            CategoryEntity(id = 3, title = "Балалайки", parentId = null),
            CategoryEntity(id = 4, title = "Укулеле", parentId = null),
            CategoryEntity(id = 5, title = "Скрипки", parentId = null),
            CategoryEntity(id = 6, title = "Аксессуары", parentId = null),

            CategoryEntity(id = 7, title = "<- Назад", parentId = 1),
            CategoryEntity(id = 8, title = "Акустические гитары", parentId = 1),
            CategoryEntity(id = 9, title = "Электрогитары", parentId = 1),
            CategoryEntity(id = 10, title = "Полу-акустика", parentId = 1),

            CategoryEntity(id = 11, title = "<- Назад", parentId = 9),
            CategoryEntity(id = 12, title = "Телекастер", parentId = 9),
            CategoryEntity(id = 13, title = "Стратокастер", parentId = 9),
            CategoryEntity(id = 14, title = "Суперстрат", parentId = 9),
            CategoryEntity(id = 15, title = "Лес-пол", parentId = 9),
            CategoryEntity(id = 16, title = "SG", parentId = 9),
            CategoryEntity(id = 17, title = "Эксплорер", parentId = 9),
            CategoryEntity(id = 18, title = "Джаз-мастер", parentId = 9),

            CategoryEntity(id = 19, title = "<- Назад", parentId = 8),
            CategoryEntity(id = 20, title = "Джамбо", parentId = 8),
            CategoryEntity(id = 21, title = "Дредноут", parentId = 8),
            CategoryEntity(id = 22, title = "Гранд Аудиториум", parentId = 8),
            CategoryEntity(id = 23, title = "Фолк", parentId = 8),
            CategoryEntity(id = 24, title = "Парлор", parentId = 8),
            CategoryEntity(id = 25, title = "Орка", parentId = 8),

            CategoryEntity(id = 26, title = "<- Назад", parentId = 10),
            CategoryEntity(id = 27, title = "Полые", parentId = 10),
            CategoryEntity(id = 28, title = "Частично полые", parentId = 10),

            CategoryEntity(id = 29, title = "<- Назад", parentId = 2),
            CategoryEntity(id = 30, title = "Электронный бас", parentId = 2),
            CategoryEntity(id = 31, title = "Акустический бас", parentId = 2),

            CategoryEntity(id = 32, title = "<- Назад", parentId = 30),
            CategoryEntity(id = 33, title = "P-Bass", parentId = 30),
            CategoryEntity(id = 34, title = "Jazz-Bass", parentId = 30),
            CategoryEntity(id = 35, title = "Fusion Bass", parentId = 30),
            CategoryEntity(id = 36, title = "Пятиструнный бас", parentId = 30),
            CategoryEntity(id = 37, title = "Шестиструнный бас", parentId = 30),

            CategoryEntity(id = 38, title = "<- Назад", parentId = 31),
            CategoryEntity(id = 39, title = "Джамбо", parentId = 31),
            CategoryEntity(id = 40, title = "Дредноут", parentId = 31),
            CategoryEntity(id = 41, title = "Гранд Аудиториум", parentId = 31),

            CategoryEntity(id = 42, title = "<- Назад", parentId = 3),
            CategoryEntity(id = 43, title = "Электронная балалайка", parentId = 3),
            CategoryEntity(id = 44, title = "Акустическая балалайка", parentId = 3),

            CategoryEntity(id = 45, title = "<- Назад", parentId = 43),
            CategoryEntity(id = 46, title = "Прима", parentId = 43),
            CategoryEntity(id = 47, title = "Секунда", parentId = 43),
            CategoryEntity(id = 48, title = "Альт", parentId = 43),
            CategoryEntity(id = 49, title = "Бас", parentId = 43),
            CategoryEntity(id = 50, title = "Контрабас", parentId = 43),

            CategoryEntity(id = 51, title = "<- Назад", parentId = 44),
            CategoryEntity(id = 52, title = "Прима", parentId = 44),
            CategoryEntity(id = 53, title = "Секунда", parentId = 44),
            CategoryEntity(id = 54, title = "Альт", parentId = 44),
            CategoryEntity(id = 55, title = "Бас", parentId = 44),
            CategoryEntity(id = 56, title = "Контрабас", parentId = 44),

            CategoryEntity(id = 57, title = "<- Назад", parentId = 4),
            CategoryEntity(id = 58, title = "Электронные укулеле", parentId = 4),
            CategoryEntity(id = 59, title = "Акустические укулеле", parentId = 4),

            CategoryEntity(id = 60, title = "<- Назад", parentId = 5),
            CategoryEntity(id = 61, title = "Электронная скрипка", parentId = 5),
            CategoryEntity(id = 62, title = "Акустическая скрипка", parentId = 5),

            CategoryEntity(id = 63, title = "<- Назад", parentId = 6),
            CategoryEntity(id = 64, title = "Медиаторы", parentId = 6),
            CategoryEntity(id = 65, title = "Струны", parentId = 6),

            CategoryEntity(id = 66, title = "<- Назад", parentId = 58),
            CategoryEntity(id = 67, title = "Сопрано", parentId = 58),
            CategoryEntity(id = 68, title = "Концерт", parentId = 58),
            CategoryEntity(id = 69, title = "Тенор", parentId = 58),
            CategoryEntity(id = 70, title = "Баритон", parentId = 58),

            CategoryEntity(id = 71, title = "<- Назад", parentId = 59),
            CategoryEntity(id = 72, title = "Сопрано", parentId = 59),
            CategoryEntity(id = 73, title = "Концерт", parentId = 59),
            CategoryEntity(id = 74, title = "Тенор", parentId = 59),
            CategoryEntity(id = 75, title = "Баритон", parentId = 59),

            CategoryEntity(id = 76, title = "<- Назад", parentId = 61),
            CategoryEntity(id = 78, title = "4/4", parentId = 61),
            CategoryEntity(id = 79, title = "7/8", parentId = 61),
            CategoryEntity(id = 80, title = "3/4", parentId = 61),
            CategoryEntity(id = 81, title = "1/2", parentId = 61),
            CategoryEntity(id = 82, title = "1/4", parentId = 61),
            CategoryEntity(id = 83, title = "1/8", parentId = 61),

            CategoryEntity(id = 84, title = "<- Назад", parentId = 62),
            CategoryEntity(id = 85, title = "4/4", parentId = 62),
            CategoryEntity(id = 86, title = "7/8", parentId = 62),
            CategoryEntity(id = 87, title = "3/4", parentId = 62),
            CategoryEntity(id = 88, title = "1/2", parentId = 62),
            CategoryEntity(id = 89, title = "1/4", parentId = 62),
            CategoryEntity(id = 90, title = "1/8", parentId = 62),

            CategoryEntity(id = 91, title = "<- Назад", parentId = 64),
            CategoryEntity(id = 92, title = "Пластиковые", parentId = 64),
            CategoryEntity(id = 93, title = "Металлические", parentId = 64),

            CategoryEntity(id = 94, title = "<- Назад", parentId = 65),
            CategoryEntity(id = 95, title = "Нейлоновые гитарные", parentId = 65),
            CategoryEntity(id = 96, title = "Металлические гитарные", parentId = 65),
            CategoryEntity(id = 97, title = "Для бас гитары", parentId = 65),
            CategoryEntity(id = 98, title = "Для балалайки", parentId = 65),
            CategoryEntity(id = 99, title = "Для укулеле", parentId = 65),
            CategoryEntity(id = 100, title = "Для скрипки", parentId = 65)
        )

        dao.insertAll(data)
    }

    private fun navigateBack() {
        if (parentStack.isNotEmpty()) {
            parentStack.removeAt(parentStack.lastIndex)
            currentParentId = parentStack.lastOrNull()
            loadCategories(currentParentId)
        }
    }

    private fun openCategory(id: Int) {
        parentStack.add(currentParentId)
        currentParentId = id
        loadCategories(id)
    }
    private fun loadCategories(parentId: Int?) {
        lifecycleScope.launch {
            val items = if (parentId == null)
                dao.getRootCategories()
            else
                dao.getChildren(parentId)

            val adapter = object : ArrayAdapter<CategoryEntity>(
                this@MainActivity,
                R.layout.item_category,
                items) {
                override fun getItem(position: Int): CategoryEntity? = items[position]

                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    val textView = view.findViewById<TextView>(R.id.itemText)
                    textView.text = items[position].title
                    return view
                }
            }

            categoryList.adapter = adapter
        }
    }
}
