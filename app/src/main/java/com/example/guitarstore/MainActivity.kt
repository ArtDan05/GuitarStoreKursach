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
    var listPosition: Int = 0
    var choicedArr: Array<Int> = arrayOf(0, 0, 0)
    val category: Array<String> = arrayOf("Гитары",
        "Бас",
        "Балалайки",
        "Укулеле",
        "Скрипки",
        "Аксессуары")

    val guitarCategoriesType: Array<String> = arrayOf("<- Назад", "Акустические гитары", "Электрогитары", "Полу-акустика")
    val ElectroguitarCategories: Array<String> = arrayOf("<- Назад",
        "Телекастер",
        "Стратокастер",
        "Суперстрат",
        "Лес-пол",
        "SG",
        "Эксплорер",
        "Джаз-мастер")

    val AcousticGuitarsCategories: Array<String> = arrayOf("<- Назад",
        "Джамбо",
        "Дредноут",
        "Гранд Аудиториум",
        "Фолк",
        "Парлор",
        "Орка")

    val SemiAcousticGuitarsCategories: Array<String> = arrayOf("<- Назад", "Полые", "Частично полые")

    val BassCategories: Array<String> = arrayOf("<- Назад", "Электронный бас", "Акустический бас")

    val ElectroBassCategories: Array<String> = arrayOf("<- Назад", "P-Bass", "Jazz-Bass", "Fusion Bass", "Пятиструнный бас", "Шестиструнный бас")

    val AcousticBassCategories: Array<String> = arrayOf("<- Назад",
        "Джамбо",
        "Дредноут",
        "Гранд Аудиториум")

    val BalalaikaCategories: Array<String> = arrayOf("<- Назад", "Электронная балалайка", "Акустическая балалайка")
    val BalalaikaFormCategories: Array<String> = arrayOf("<- Назад", "Прима", "Секунда", "Альт", "Бас", "Контрабас")
    val UkuleleCategories: Array<String> = arrayOf("<- Назад", "Электронные укулеле", "Акустические укулеле")
    val UkuleleFormCategories: Array<String> = arrayOf("<- Назад", "Сопрано", "Концерт", "Тенор", "Баритон")
    val ViolinCategories: Array<String> = arrayOf("<- Назад", "Электронная скрипка", "Акустическая скрипка")
    val ViolinFormCategories: Array<String> = arrayOf("<- Назад", "4/4", "7/8", "3/4", "1/2", "1/4", "1/8")
    val AccessoriesCategories: Array<String> = arrayOf("<- Назад", "Медиаторы", "Струны")
    val StringsCategories: Array<String> = arrayOf("<- Назад", "Нейлоновые гитарные",
        "Металлические гитарные",
        "Для бас гитары",
        "Для балалайки",
        "Для укулеле",
        "Для скрипки")

    val MediatorTypes: Array<String> = arrayOf("<- Назад", "Пластиковые", "Металлические")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categoryList = findViewById(R.id.CategoryList)
        setListContent(category, 0, 0);

        categoryList.setOnItemClickListener { parent, view, position, id ->
            when (listPosition) {
                0 -> { when (position) {
                    0 -> setListContent(guitarCategoriesType, 1, 0)
                    1 -> setListContent(BassCategories, 1, 1)
                    2 -> setListContent(BalalaikaCategories, 1, 2)
                    3 -> setListContent(UkuleleCategories, 1, 3)
                    4 -> setListContent(ViolinCategories, 1, 4)
                    5 -> setListContent(AccessoriesCategories, 1, 5)
                    }
                }

                1 -> {
                    secondStageOfListHelpMePls(position)
                }

                2 -> {

                }
            }
       }
    }

    fun setListContent(data: Array<String>, listPositionInt: Int, choicedArrInt: Int) {
        val adapter = ArrayAdapter(this, R.layout.item_category, data)
        categoryList.adapter = adapter
        listPosition = listPositionInt
        choicedArr[listPositionInt] = choicedArrInt
    }

    fun secondStageOfListHelpMePls(position: Int) {
        when (choicedArr[0]) {
            0 -> {
                when (position) {
                    0 -> setListContent(category, 0, 0)
                    1 -> setListContent(AcousticGuitarsCategories, 2, 1)
                    2 -> setListContent(ElectroguitarCategories, 2, 2)
                    3 -> setListContent(SemiAcousticGuitarsCategories, 2, 3)
                }
            }

            1 -> {
                when (position) {
                    0 -> setListContent(category, 0, 0)
                    1 -> setListContent(ElectroBassCategories, 2, 1)
                    2 -> setListContent(AcousticBassCategories, 2, 2)
                }
            }

            2 -> {
                when (position) {
                    0 -> setListContent(category, 0, 0)
                    1 -> setListContent(BalalaikaFormCategories, 2, 1)
                    2 -> setListContent(BalalaikaFormCategories, 2, 2)
                }
            }

            3 -> {
                when (position) {
                    0 -> setListContent(category, 0, 0)
                    1 -> setListContent(UkuleleFormCategories, 2, 1)
                    2 -> setListContent(UkuleleFormCategories, 2, 2)
                }
            }

            4 -> {
                when (position) {
                    0 -> setListContent(category, 0, 0)
                    1 -> setListContent(ViolinFormCategories, 2, 1)
                    2 -> setListContent(ViolinFormCategories, 2, 2)
                }
            }

            5 -> {
                when (position) {
                    0 -> setListContent(category, 0, 0)
                    1 -> setListContent(StringsCategories, 2, 1)
                    2 -> setListContent(MediatorTypes, 2, 2)
                }
            }
        }
    }

    fun ThirdStageOfListThisIsTooMuch(position: Int) {
        when (position) {
            0 -> secondStageOfListHelpMePls(choicedArr[1])
            else -> choicedArr[2] = position
        }
    }
}