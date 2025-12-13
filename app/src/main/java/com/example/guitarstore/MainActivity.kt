package com.example.guitarstore

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import androidx.core.graphics.scale

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigation: BottomNavigationView
    lateinit var placeholderDrawable: Drawable
    lateinit var placeholderBitmap: Bitmap
    lateinit var placeholderByte: ByteArray
    lateinit var productsDao: ProductDao

    private lateinit var controlHub: ControlHub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DatabaseInstance.init(this)

        bottomNavigation = findViewById(R.id.bottom_navigation)

        controlHub = ViewModelProvider(this)[ControlHub::class.java]

        lifecycleScope.launch {
            placeholderDrawable = resources.getDrawable(R.drawable.fender_placeholder)
            placeholderBitmap = (placeholderDrawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            placeholderBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            placeholderByte = stream.toByteArray()

            preloadDataIfNeeded()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, CategoryFragment())
            .commit()

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.cartFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, CartFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }

                R.id.catalogFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, CategoryFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }

                else -> false
            }
        }
    }

    private suspend fun preloadDataIfNeeded() {
        val dao = DatabaseInstance.db.categoryDao()
        productsDao = DatabaseInstance.db.productDao()

        val count = dao.getCount()
        val countProd = productsDao.getCount()
        if (count > 0 && countProd > 0) return

        val data = listOf(
            CategoryEntity(id = 1, title = "Гитары", parentId = null),
            CategoryEntity(id = 2, title = "Бас", parentId = null),
            CategoryEntity(id = 3, title = "Балалайки", parentId = null),
            CategoryEntity(id = 4, title = "Укулеле", parentId = null),
            CategoryEntity(id = 5, title = "Скрипки", parentId = null),
            CategoryEntity(id = 6, title = "Аксессуары", parentId = null),

            CategoryEntity(id = 8, title = "Акустические гитары", parentId = 1),
            CategoryEntity(id = 9, title = "Электрогитары", parentId = 1),
            CategoryEntity(id = 10, title = "Полу-акустика", parentId = 1),

            CategoryEntity(id = 12, title = "Телекастер", parentId = 9),
            CategoryEntity(id = 13, title = "Стратокастер", parentId = 9),
            CategoryEntity(id = 14, title = "Суперстрат", parentId = 9),
            CategoryEntity(id = 15, title = "Лес-пол", parentId = 9),
            CategoryEntity(id = 16, title = "SG", parentId = 9),
            CategoryEntity(id = 17, title = "Эксплорер", parentId = 9),
            CategoryEntity(id = 18, title = "Джаз-мастер", parentId = 9),

            CategoryEntity(id = 20, title = "Джамбо", parentId = 8),
            CategoryEntity(id = 21, title = "Дредноут", parentId = 8),
            CategoryEntity(id = 22, title = "Гранд Аудиториум", parentId = 8),
            CategoryEntity(id = 23, title = "Фолк", parentId = 8),
            CategoryEntity(id = 24, title = "Парлор", parentId = 8),
            CategoryEntity(id = 25, title = "Орка", parentId = 8),

            CategoryEntity(id = 27, title = "Полые", parentId = 10),
            CategoryEntity(id = 28, title = "Частично полые", parentId = 10),

            CategoryEntity(id = 30, title = "Электронный бас", parentId = 2),
            CategoryEntity(id = 31, title = "Акустический бас", parentId = 2),

            CategoryEntity(id = 33, title = "P-Bass", parentId = 30),
            CategoryEntity(id = 34, title = "Jazz-Bass", parentId = 30),
            CategoryEntity(id = 35, title = "Fusion Bass", parentId = 30),
            CategoryEntity(id = 36, title = "Пятиструнный бас", parentId = 30),
            CategoryEntity(id = 37, title = "Шестиструнный бас", parentId = 30),

            CategoryEntity(id = 39, title = "Джамбо", parentId = 31),
            CategoryEntity(id = 40, title = "Дредноут", parentId = 31),
            CategoryEntity(id = 41, title = "Гранд Аудиториум", parentId = 31),

            CategoryEntity(id = 43, title = "Электронная балалайка", parentId = 3),
            CategoryEntity(id = 44, title = "Акустическая балалайка", parentId = 3),

            CategoryEntity(id = 46, title = "Прима", parentId = 43),
            CategoryEntity(id = 47, title = "Секунда", parentId = 43),
            CategoryEntity(id = 48, title = "Альт", parentId = 43),
            CategoryEntity(id = 49, title = "Бас", parentId = 43),
            CategoryEntity(id = 50, title = "Контрабас", parentId = 43),

            CategoryEntity(id = 52, title = "Прима", parentId = 44),
            CategoryEntity(id = 53, title = "Секунда", parentId = 44),
            CategoryEntity(id = 54, title = "Альт", parentId = 44),
            CategoryEntity(id = 55, title = "Бас", parentId = 44),
            CategoryEntity(id = 56, title = "Контрабас", parentId = 44),

            CategoryEntity(id = 58, title = "Электронные укулеле", parentId = 4),
            CategoryEntity(id = 59, title = "Акустические укулеле", parentId = 4),

            CategoryEntity(id = 61, title = "Электронная скрипка", parentId = 5),
            CategoryEntity(id = 62, title = "Акустическая скрипка", parentId = 5),

            CategoryEntity(id = 64, title = "Медиаторы", parentId = 6),
            CategoryEntity(id = 65, title = "Струны", parentId = 6),

            CategoryEntity(id = 67, title = "Сопрано", parentId = 58),
            CategoryEntity(id = 68, title = "Концерт", parentId = 58),
            CategoryEntity(id = 69, title = "Тенор", parentId = 58),
            CategoryEntity(id = 70, title = "Баритон", parentId = 58),

            CategoryEntity(id = 72, title = "Сопрано", parentId = 59),
            CategoryEntity(id = 73, title = "Концерт", parentId = 59),
            CategoryEntity(id = 74, title = "Тенор", parentId = 59),
            CategoryEntity(id = 75, title = "Баритон", parentId = 59),

            CategoryEntity(id = 78, title = "4/4", parentId = 61),
            CategoryEntity(id = 79, title = "7/8", parentId = 61),
            CategoryEntity(id = 80, title = "3/4", parentId = 61),
            CategoryEntity(id = 81, title = "1/2", parentId = 61),
            CategoryEntity(id = 82, title = "1/4", parentId = 61),
            CategoryEntity(id = 83, title = "1/8", parentId = 61),

            CategoryEntity(id = 85, title = "4/4", parentId = 62),
            CategoryEntity(id = 86, title = "7/8", parentId = 62),
            CategoryEntity(id = 87, title = "3/4", parentId = 62),
            CategoryEntity(id = 88, title = "1/2", parentId = 62),
            CategoryEntity(id = 89, title = "1/4", parentId = 62),
            CategoryEntity(id = 90, title = "1/8", parentId = 62),

            CategoryEntity(id = 92, title = "Пластиковые", parentId = 64),
            CategoryEntity(id = 93, title = "Металлические", parentId = 64),

            CategoryEntity(id = 95, title = "Нейлоновые гитарные", parentId = 65),
            CategoryEntity(id = 96, title = "Металлические гитарные", parentId = 65),
            CategoryEntity(id = 97, title = "Для бас гитары", parentId = 65),
            CategoryEntity(id = 98, title = "Для балалайки", parentId = 65),
            CategoryEntity(id = 99, title = "Для укулеле", parentId = 65),
            CategoryEntity(id = 100, title = "Для скрипки", parentId = 65)
        )
        if (count == 0) dao.insertAll(data)

        val products = listOf(
            ProductEntity(id = 0, title = "Fender Player II Stratocaster", price = 71560, image = placeholderByte, 1, 9, 13),
            ProductEntity(id = 1, title = "Fabio ST100 BLS", price = 9000, image = placeholderByte, 1, 9, 13),
            ProductEntity(id = 2, title = "Fender Squier Classic", price = 40160, image = placeholderByte, 1, 9, 12),
            ProductEntity(id = 3, title = "Oscar Schmidt OS-LT IV", price = 12000, image = placeholderByte, 1, 9, 12),
            ProductEntity(id = 4, title = "Terris TSS-239 BK", price = 15450, image = placeholderByte, 1, 9, 14),
            ProductEntity(id = 5, title = "Jet JS-501 BK", price = 57850, image = placeholderByte, 1, 9, 14),
            ProductEntity(id = 6, title = "Gibson Les Paul Standard 60s", price = 608000, image = placeholderByte, 1, 9, 15),
            ProductEntity(id = 7, title = "Fabio LP02 BK", price = 16000, image = placeholderByte, 1, 9, 15),
            ProductEntity(id = 8, title = "Gibson SG Standard", price = 140000, image = placeholderByte, 1, 9, 16),
            ProductEntity(id = 9, title = "Gibson Epiphone SG Tribute Plus", price = 37500, image = placeholderByte, 1, 9, 16),
            ProductEntity(id = 10, title = "Gibson `70s Explorer CW", price = 203500, image = placeholderByte, 1, 9, 17),
            ProductEntity(id = 11, title = "Adam Firebird", price = 15000, image = placeholderByte, 1, 9, 17),
            ProductEntity(id = 12, title = "Fender AV II 66", price = 222300, image = placeholderByte, 1, 9, 18),
            ProductEntity(id = 13, title = "Fender Squier Classic Vibe `60s", price = 45700, image = placeholderByte, 1, 9, 18),

            ProductEntity(id = 14, title = "Yamaha FG800 Джамбо", price = 24500, image = placeholderByte, 1, 8, 20),
            ProductEntity(id = 15, title = "Fender FA-125 Джамбо", price = 18900, image = placeholderByte, 1, 8, 20),
            ProductEntity(id = 16, title = "Cort AD810 Дредноут", price = 15600, image = placeholderByte, 1, 8, 21),
            ProductEntity(id = 17, title = "Epiphone DR-100 Дредноут", price = 17800, image = placeholderByte, 1, 8, 21),
            ProductEntity(id = 18, title = "Taylor 214ce Гранд Аудиториум", price = 125000, image = placeholderByte, 1, 8, 22),
            ProductEntity(id = 19, title = "Crafter D-7 Гранд Аудиториум", price = 32500, image = placeholderByte, 1, 8, 22),
            ProductEntity(id = 20, title = "Ibanez V50NJP Фолк", price = 14200, image = placeholderByte, 1, 8, 23),
            ProductEntity(id = 21, title = "Fender CD-60SCE Фолк", price = 29500, image = placeholderByte, 1, 8, 23),
            ProductEntity(id = 22, title = "Gretsch G9500 Парлор", price = 18700, image = placeholderByte, 1, 8, 24),
            ProductEntity(id = 23, title = "Sigma 000MC-1ST Парлор", price = 23500, image = placeholderByte, 1, 8, 24),
            ProductEntity(id = 24, title = "Ortega R121-7 Орка", price = 43200, image = placeholderByte, 1, 8, 25),
            ProductEntity(id = 25, title = "Furch Blue Gc-CM Орка", price = 89000, image = placeholderByte, 1, 8, 25),

            ProductEntity(id = 26, title = "Epiphone Casino Полые", price = 67500, image = placeholderByte, 1, 10, 27),
            ProductEntity(id = 27, title = "Ibanez AS73 Полые", price = 45600, image = placeholderByte, 1, 10, 27),
            ProductEntity(id = 28, title = "Gibson ES-335 Частично полые", price = 310000, image = placeholderByte, 1, 10, 28),
            ProductEntity(id = 29, title = "Epiphone Dot Частично полые", price = 49800, image = placeholderByte, 1, 10, 28),

            ProductEntity(id = 30, title = "Fender Player Precision Bass", price = 78500, image = placeholderByte, 2, 30, 33),
            ProductEntity(id = 31, title = "Squier Affinity Precision Bass", price = 24500, image = placeholderByte, 2, 30, 33),
            ProductEntity(id = 32, title = "Fender American Professional Jazz Bass", price = 156000, image = placeholderByte, 2, 30, 34),
            ProductEntity(id = 33, title = "Ibanez GSR200 Jazz Bass", price = 19800, image = placeholderByte, 2, 30, 34),
            ProductEntity(id = 34, title = "Yamaha TRBX504 Fusion Bass", price = 56700, image = placeholderByte, 2, 30, 35),
            ProductEntity(id = 35, title = "Cort GB74JJ Fusion Bass", price = 43200, image = placeholderByte, 2, 30, 35),
            ProductEntity(id = 36, title = "Ibanez SR505 Пятиструнный", price = 65400, image = placeholderByte, 2, 30, 36),
            ProductEntity(id = 37, title = "Spector Legend 5 Пятиструнный", price = 78900, image = placeholderByte, 2, 30, 36),
            ProductEntity(id = 38, title = "Schecter Stiletto Studio 6 Шестиструнный", price = 98500, image = placeholderByte, 2, 30, 37),
            ProductEntity(id = 39, title = "Ibanez GSR206 Шестиструнный", price = 25600, image = placeholderByte, 2, 30, 37),

            ProductEntity(id = 40, title = "Taylor GS Mini-e Bass Джамбо", price = 123000, image = placeholderByte, 2, 31, 39),
            ProductEntity(id = 41, title = "Fender Kingman Bass Джамбо", price = 76500, image = placeholderByte, 2, 31, 39),
            ProductEntity(id = 42, title = "Ibanez AEB10E Дредноут", price = 43200, image = placeholderByte, 2, 31, 40),
            ProductEntity(id = 43, title = "Crafter BA-400 Дредноут", price = 56700, image = placeholderByte, 2, 31, 40),
            ProductEntity(id = 44, title = "Gibson J-45 Bass Гранд Аудиториум", price = 210000, image = placeholderByte, 2, 31, 41),
            ProductEntity(id = 45, title = "Martin BC-15E Гранд Аудиториум", price = 145000, image = placeholderByte, 2, 31, 41),

            ProductEntity(id = 46, title = "Русская Балалайка Электро Прима", price = 34500, image = placeholderByte, 3, 43, 46),
            ProductEntity(id = 47, title = "Теремок Электро Прима", price = 27800, image = placeholderByte, 3, 43, 46),
            ProductEntity(id = 48, title = "Русская Балалайка Электро Секунда", price = 36500, image = placeholderByte, 3, 43, 47),
            ProductEntity(id = 49, title = "Теремок Электро Секунда", price = 29800, image = placeholderByte, 3, 43, 47),
            ProductEntity(id = 50, title = "Русская Балалайка Электро Альт", price = 41200, image = placeholderByte, 3, 43, 48),
            ProductEntity(id = 51, title = "Теремок Электро Альт", price = 34500, image = placeholderByte, 3, 43, 48),
            ProductEntity(id = 52, title = "Русская Балалайка Электро Бас", price = 56700, image = placeholderByte, 3, 43, 49),
            ProductEntity(id = 53, title = "Теремок Электро Бас", price = 48900, image = placeholderByte, 3, 43, 49),
            ProductEntity(id = 54, title = "Русская Балалайка Электро Контрабас", price = 78900, image = placeholderByte, 3, 43, 50),
            ProductEntity(id = 55, title = "Теремок Электро Контрабас", price = 65400, image = placeholderByte, 3, 43, 50),

            ProductEntity(id = 56, title = "Русская Балалайка Прима", price = 15600, image = placeholderByte, 3, 44, 52),
            ProductEntity(id = 57, title = "Теремок Прима", price = 12300, image = placeholderByte, 3, 44, 52),
            ProductEntity(id = 58, title = "Русская Балалайка Секунда", price = 17800, image = placeholderByte, 3, 44, 53),
            ProductEntity(id = 59, title = "Теремок Секунда", price = 14500, image = placeholderByte, 3, 44, 53),
            ProductEntity(id = 60, title = "Русская Балалайка Альт", price = 21200, image = placeholderByte, 3, 44, 54),
            ProductEntity(id = 61, title = "Теремок Альт", price = 18900, image = placeholderByte, 3, 44, 54),
            ProductEntity(id = 62, title = "Русская Балалайка Бас", price = 34500, image = placeholderByte, 3, 44, 55),
            ProductEntity(id = 63, title = "Теремок Бас", price = 29800, image = placeholderByte, 3, 44, 55),
            ProductEntity(id = 64, title = "Русская Балалайка Контрабас", price = 56700, image = placeholderByte, 3, 44, 56),
            ProductEntity(id = 65, title = "Теремок Контрабас", price = 48900, image = placeholderByte, 3, 44, 56),

            ProductEntity(id = 66, title = "Kala KA-15S Сопрано", price = 8900, image = placeholderByte, 4, 58, 67),
            ProductEntity(id = 67, title = "Mahalo U-30 Сопрано", price = 4500, image = placeholderByte, 4, 58, 67),
            ProductEntity(id = 68, title = "Cordoba 15CM Концерт", price = 15600, image = placeholderByte, 4, 58, 68),
            ProductEntity(id = 69, title = "Flight TUC-34 Концерт", price = 11200, image = placeholderByte, 4, 58, 68),
            ProductEntity(id = 70, title = "Kala KA-ATP-CTG Тенор", price = 24500, image = placeholderByte, 4, 58, 69),
            ProductEntity(id = 71, title = "Cordoba 20TM Тенор", price = 18900, image = placeholderByte, 4, 58, 69),
            ProductEntity(id = 72, title = "Kala KA-ABP-CTG Баритон", price = 29800, image = placeholderByte, 4, 58, 70),
            ProductEntity(id = 73, title = "Cordoba 24B Баритон", price = 25600, image = placeholderByte, 4, 58, 70),

            ProductEntity(id = 74, title = "Flight NUS310 Сопрано", price = 5600, image = placeholderByte, 4, 59, 72),
            ProductEntity(id = 75, title = "Hora U-10 Сопрано", price = 3400, image = placeholderByte, 4, 59, 72),
            ProductEntity(id = 76, title = "Kala KA-C Концерт", price = 12300, image = placeholderByte, 4, 59, 73),
            ProductEntity(id = 77, title = "Mahalo MK2 Концерт", price = 6700, image = placeholderByte, 4, 59, 73),
            ProductEntity(id = 78, title = "Cordoba 22T Тенор", price = 18700, image = placeholderByte, 4, 59, 74),
            ProductEntity(id = 79, title = "Flight NUT430 Тенор", price = 14500, image = placeholderByte, 4, 59, 74),
            ProductEntity(id = 80, title = "Kala KA-BG Баритон", price = 23400, image = placeholderByte, 4, 59, 75),
            ProductEntity(id = 81, title = "Cordoba 24B Акустика Баритон", price = 19800, image = placeholderByte, 4, 59, 75),

            ProductEntity(id = 82, title = "Yamaha YEV104 4/4", price = 87600, image = placeholderByte, 5, 61, 78),
            ProductEntity(id = 83, title = "Cecilio CVN-500 4/4", price = 34500, image = placeholderByte, 5, 61, 78),
            ProductEntity(id = 84, title = "Stagg EVN 7/8", price = 45600, image = placeholderByte, 5, 61, 79),
            ProductEntity(id = 85, title = "Barcus Berry 7/8", price = 67800, image = placeholderByte, 5, 61, 79),
            ProductEntity(id = 86, title = "NS Design WAV4 3/4", price = 54300, image = placeholderByte, 5, 61, 80),
            ProductEntity(id = 87, title = "Zeta Jazz 3/4", price = 123000, image = placeholderByte, 5, 61, 80),
            ProductEntity(id = 88, title = "Yamaha YEV104 1/2", price = 76500, image = placeholderByte, 5, 61, 81),
            ProductEntity(id = 89, title = "Cecilio CVN-500 1/2", price = 29800, image = placeholderByte, 5, 61, 81),
            ProductEntity(id = 90, title = "Stagg EVN 1/4", price = 38700, image = placeholderByte, 5, 61, 82),
            ProductEntity(id = 91, title = "Barcus Berry 1/4", price = 45600, image = placeholderByte, 5, 61, 82),
            ProductEntity(id = 92, title = "NS Design WAV4 1/8", price = 42300, image = placeholderByte, 5, 61, 83),
            ProductEntity(id = 93, title = "Zeta Jazz 1/8", price = 98700, image = placeholderByte, 5, 61, 83),

            ProductEntity(id = 94, title = "Stradivarius Copy 4/4", price = 45600, image = placeholderByte, 5, 62, 85),
            ProductEntity(id = 95, title = "Mendini MV500 4/4", price = 23400, image = placeholderByte, 5, 62, 85),
            ProductEntity(id = 96, title = "Cremona SV-500 7/8", price = 56700, image = placeholderByte, 5, 62, 86),
            ProductEntity(id = 97, title = "Stentor 7/8", price = 43200, image = placeholderByte, 5, 62, 86),
            ProductEntity(id = 98, title = "Yamaha V3SKA 3/4", price = 65400, image = placeholderByte, 5, 62, 87),
            ProductEntity(id = 99, title = "Cremona SV-175 3/4", price = 34500, image = placeholderByte, 5, 62, 87),
            ProductEntity(id = 100, title = "Mendini MV200 1/2", price = 19800, image = placeholderByte, 5, 62, 88),
            ProductEntity(id = 101, title = "Stentor Student I 1/2", price = 25600, image = placeholderByte, 5, 62, 88),
            ProductEntity(id = 102, title = "Yamaha V5SKA 1/4", price = 23400, image = placeholderByte, 5, 62, 89),
            ProductEntity(id = 103, title = "Cremona SV-100 1/4", price = 18700, image = placeholderByte, 5, 62, 89),
            ProductEntity(id = 104, title = "Mendini MV50 1/8", price = 15600, image = placeholderByte, 5, 62, 90),
            ProductEntity(id = 105, title = "Stentor Student I 1/8", price = 19800, image = placeholderByte, 5, 62, 90),

            ProductEntity(id = 106, title = "Dunlop Tortex 0.60mm Пластиковые", price = 150, image = placeholderByte, 6, 64, 92),
            ProductEntity(id = 107, title = "Jim Dunlop Nylon Standard Пластиковые", price = 120, image = placeholderByte, 6, 64, 92),
            ProductEntity(id = 108, title = "Dunlop Jazz III Металлические", price = 250, image = placeholderByte, 6, 64, 93),
            ProductEntity(id = 109, title = "Planet Waves Black Ice Металлические", price = 180, image = placeholderByte, 6, 64, 93),

            ProductEntity(id = 110, title = "D'Addario EJ27 Нейлоновые", price = 1200, image = placeholderByte, 6, 65, 95),
            ProductEntity(id = 111, title = "Savarez 500AR Нейлоновые", price = 1800, image = placeholderByte, 6, 65, 95),
            ProductEntity(id = 112, title = "Ernie Ball 2221 Металлические", price = 900, image = placeholderByte, 6, 65, 96),
            ProductEntity(id = 113, title = "Elixir Nanoweb Металлические", price = 2100, image = placeholderByte, 6, 65, 96),
            ProductEntity(id = 114, title = "Rotosound RB45 Для бас гитары", price = 1600, image = placeholderByte, 6, 65, 97),
            ProductEntity(id = 115, title = "D'Addario EXL170 Для бас гитары", price = 1400, image = placeholderByte, 6, 65, 97),
            ProductEntity(id = 116, title = "Первый Струнный Для балалайки", price = 800, image = placeholderByte, 6, 65, 98),
            ProductEntity(id = 117, title = "Русские Струны Для балалайки", price = 950, image = placeholderByte, 6, 65, 98),
            ProductEntity(id = 118, title = "Aquila Nylgut Для укулеле", price = 750, image = placeholderByte, 6, 65, 99),
            ProductEntity(id = 119, title = "D'Addario EJ87S Для укулеле", price = 1100, image = placeholderByte, 6, 65, 99),
            ProductEntity(id = 120, title = "Thomastik Dominant Для скрипки", price = 3200, image = placeholderByte, 6, 65, 100),
            ProductEntity(id = 121, title = "Pirastro Tonica Для скрипки", price = 2800, image = placeholderByte, 6, 65, 100)
        )
        if (countProd == 0) productsDao.insertAll(products)
    }

    fun navigateBack(view: View) {

    }

    fun getImageFromDB(byteArray: ByteArray): Bitmap {
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return bmp.scale(512, 512, false)
    }

    fun removeButtonClick(view: View) {
        val id = view.tag as Int
        lifecycleScope.launch {
            CartManager.remove(productsDao.getProductByID(id))
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, CartFragment())
            .addToBackStack(null)
            .commit()
    }
}
