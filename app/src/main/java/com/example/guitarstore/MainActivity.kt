package com.example.guitarstore

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.Array
import kotlin.String

class MainActivity : AppCompatActivity() {
    lateinit var categoryList: ListView
    val category: Array<String> = arrayOf("Гитары",
        "Бас",
        "Балалайки",
        "Укулеле",
        "Скрипки",
        "Аксессуары")

    val guitarCategoriesType: Array<String> = arrayOf("Акустические гитары", "Электрогитары", "Полу-акустика")
    val ElectroguitarCategories: Array<String> = arrayOf("Телекастер",
        "Стратокастер",
        "Суперстрат",
        "Лес-пол",
        "SG",
        "Эксплорер",
        "Джаз-мастер")

    val AcousticGuitarsCategories: Array<String> = arrayOf("Джамбо",
        "Дредноут",
        "Гранд Аудиториум",
        "Фолк",
        "Парлор",
        "Орка")

    val SemiAcousticGuitarsCategories: Array<String> = arrayOf("Полые", "Частично полые")

    val BassCategories: Array<String> = arrayOf("Электронный бас", "Акустический бас")

    val ElectroBassCategories: Array<String> = arrayOf("P-Bass", "Jazz-Bass", "Fusion Bass", "Пятиструнный бас", "Шестиструнный бас")

    val AcousticBassCategories: Array<String> = arrayOf("Джамбо",
        "Дредноут",
        "Гранд Аудиториум")

    val BalalaikaCategories: Array<String> = arrayOf("Электронная балалайка", "Акустическая балалайка")
    val BalalaikaFormCategories: Array<String> = arrayOf("Прима", "Секунда", "Альт", "Бас", "Контрабас")
    val UkuleleCategories: Array<String> = arrayOf("Электронные укулеле", "Акустические укулеле")
    val UkuleleFormCategories: Array<String> = arrayOf("Сопрано", "Концерт", "Тенор", "Баритон")
    val ViolinCategories: Array<String> = arrayOf("Электронная скрипка", "Акустическая скрипка")
    val ViolinFormCategories: Array<String> = arrayOf("4/4", "7/8", "3/4", "1/2", "1/4", "1/8")
    val AccessoriesCategories: Array<String> = arrayOf("Медиаторы", "Струны")
    val StringsCategories: Array<String> = arrayOf("Нейлоновые гитарные",
        "Металлические гитарные",
        "Для бас гитары",
        "Для балалайки",
        "Для укулеле",
        "Для скрипки")

    val MediatorTypes: Array<String> = arrayOf("Пластиковые", "Металлические")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categoryList = findViewById(R.id.CategoryList)
        var adapter = ArrayAdapter<String>(this, R.layout.activity_main, category)
        categoryList.adapter = adapter
    }
}