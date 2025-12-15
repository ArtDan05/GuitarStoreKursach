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
    lateinit var ordersDao: OrdersDao
    lateinit var descriptionDao: ItemDescriptionDao

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

                R.id.profileFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, ProfileFragment())
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
        ordersDao = DatabaseInstance.db.ordersDao()
        descriptionDao = DatabaseInstance.db.itemDescriptionDao()

        val count = dao.getCount()
        val countProd = productsDao.getCount()
        val countDescription = descriptionDao.getCount()
        if (count > 0 && countProd > 0 && countDescription > 0) return

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

        val description = listOf(
            ItemDescriptionEntity(itemId = 0, description = "Fender Player II Stratocaster - современная интерпретация классики. Корпус из ольхи обеспечивает сбалансированный звук с четкими верхами и насыщенными низами. Оснащена тремя синглами Alnico V, 5-позиционным переключателем и двухточечным тремоло."),
            ItemDescriptionEntity(itemId = 1, description = "Fabio ST100 BLS - доступная электрогитара в форм-факторе стратокастер. Корпус из липы, 22 лада, стандартный тремоло-бридж. Идеальна для начинающих гитаристов."),

            ItemDescriptionEntity(itemId = 2, description = "Fender Squier Classic - классический телекастер по доступной цене. Корпус из тополя, один бриджевый звукосниматель. Прямой, атакующий звук, идеальный для кантри и рок-н-ролла."),
            ItemDescriptionEntity(itemId = 3, description = "Oscar Schmidt OS-LT IV - электрогитара с корпусом из красного дерева. Два сингла, фиксированный бридж. Теплый, округлый звук с хорошим сустейном."),

            ItemDescriptionEntity(itemId = 4, description = "Terris TSS-239 BK - суперстрат с хамбакером в бриджевой позиции. 24 лада, тремоло-бридж. Агрессивный звук для хард-рока и металла."),
            ItemDescriptionEntity(itemId = 5, description = "Jet JS-501 BK - профессиональный суперстрат с хамбакерами EMG. Кленовый гриф, корпус из ольхи. Идеальна для современных тяжелых стилей."),

            ItemDescriptionEntity(itemId = 6, description = "Gibson Les Paul Standard 60s - легендарная модель с звукоснимателями BurstBucker. Корпус из красного дерева с кленовым топом. Теплый, насыщенный звук с длинным сустейном."),
            ItemDescriptionEntity(itemId = 7, description = "Fabio LP02 BK - доступная версия Les Paul. Два хамбакера, фиксированный бридж. Корпус из липы обеспечивает сбалансированный звук."),

            ItemDescriptionEntity(itemId = 8, description = "Gibson SG Standard - легкая и эргономичная гитара с характерным звучанием. Два хамбакера 490R/498T, тонкий корпус из красного дерева."),
            ItemDescriptionEntity(itemId = 9, description = "Gibson Epiphone SG Tribute Plus - более доступная версия SG с хамбакерами Probucker. Отличное качество сборки по разумной цене."),

            ItemDescriptionEntity(itemId = 10, description = "Gibson `70s Explorer CW - культовая модель с агрессивным дизайном. Хамбакеры 498T/490R, корпус из красного дерева. Мощный звук для хард-рока."),
            ItemDescriptionEntity(itemId = 11, description = "Adam Firebird - бюджетный вариант Explorer. Два хамбакера, фиксированный бридж. Стильный дизайн по доступной цене."),

            ItemDescriptionEntity(itemId = 12, description = "Fender AV II 66 - точная реплика джаз-мастера 1966 года. Одноконтурная электроника, плавающий бридж. Идеальна для альтернативного рока и сёрфа."),
            ItemDescriptionEntity(itemId = 13, description = "Fender Squier Classic Vibe `60s - доступная версия джаз-мастера. Два сингла, тремоло-бридж. Аутентичный винтажный звук."),

            ItemDescriptionEntity(itemId = 14, description = "Yamaha FG800 - классическая гитара джамбо с корпусом из ели и нато. Громкий, насыщенный звук с хорошим балансом."),
            ItemDescriptionEntity(itemId = 15, description = "Fender FA-125 - дредноут для начинающих. Корпус из ламината, гриф из нато. Отличный вариант для обучения."),

            ItemDescriptionEntity(itemId = 16, description = "Cort AD810 - классический дредноут с корпусом из ели и красного дерева. Мощный, направленный звук, идеален для аккомпанемента."),
            ItemDescriptionEntity(itemId = 17, description = "Epiphone DR-100 - доступный дредноут с корпусом из ели и красного дерева. Традиционный звук Martin-style."),

            ItemDescriptionEntity(itemId = 18, description = "Taylor 214ce - гитара гранд-аудиториум с встроенным звукоснимателем. Корпус из ламината, гриф из красного дерева. Четкий, сбалансированный звук."),
            ItemDescriptionEntity(itemId = 19, description = "Crafter D-7 - доступная гитара формы гранд-аудиториум. Корпус из ели и сапеле. Универсальный инструмент для разных стилей."),

            ItemDescriptionEntity(itemId = 20, description = "Ibanez V50NJP - фолк-гитара с узким корпусом. Идеальна для музыкантов с небольшим телосложением. Мягкий, камерный звук."),
            ItemDescriptionEntity(itemId = 21, description = "Fender CD-60SCE - электроакустическая фолк-гитара с встроенным тюнером. Отличный выбор для выступлений."),

            ItemDescriptionEntity(itemId = 22, description = "Gretsch G9500 - гитара парлор с корпусом из красного дерева. Небольшой размер, теплый, насыщенный звук. Идеальна для блюза."),
            ItemDescriptionEntity(itemId = 23, description = "Sigma 000MC-1ST - парлор с кедровым топом. Мягкий, обволакивающий звук с красивыми средними частотами."),

            ItemDescriptionEntity(itemId = 24, description = "Ortega R121-7 - 7-струнная гитара орка. Увеличенный диапазон, глубокий бас. Идеальна для фингерстайла."),
            ItemDescriptionEntity(itemId = 25, description = "Furch Blue Gc-CM - премиальная гитара орка с кедровым топом. Исключительное качество изготовления и звучания."),

            ItemDescriptionEntity(itemId = 26, description = "Epiphone Casino - полностью полый корпус, два сингла P-90. Легендарный звук, известный по записям The Beatles."),
            ItemDescriptionEntity(itemId = 27, description = "Ibanez AS73 - полуакустика с полым корпусом. Два хамбакера, стиль ES-335. Универсальный инструмент для джаза и блюза."),

            ItemDescriptionEntity(itemId = 28, description = "Gibson ES-335 - легендарная полуакустика с центральным блоком. Два хамбакера, идеальный баланс между акустическим и электрическим звучанием."),
            ItemDescriptionEntity(itemId = 29, description = "Epiphone Dot - доступная версия ES-335. Корпус из клена, два хамбакера. Отличный звук по разумной цене."),

            ItemDescriptionEntity(itemId = 30, description = "Fender Player Precision Bass - классический P-Bass с одним синглом. Плотный, фундаментальный звук, основа большинства басовых партий."),
            ItemDescriptionEntity(itemId = 31, description = "Squier Affinity Precision Bass - доступный P-Bass для начинающих. Корпус из ольхи, классический звук Precision."),

            ItemDescriptionEntity(itemId = 32, description = "Fender American Professional Jazz Bass - профессиональный J-Bass с двумя синглами. Яркий, четкий звук с выразительными верхами."),
            ItemDescriptionEntity(itemId = 33, description = "Ibanez GSR200 - бюджетный J-Bass стиль. Легкий корпус, быстрый гриф. Идеален для начинающих."),

            ItemDescriptionEntity(itemId = 34, description = "Yamaha TRBX504 - бас-гитара для фьюжн и современных стилей. Активная электроника, два хамбакера. Широкий тональный диапазон."),
            ItemDescriptionEntity(itemId = 35, description = "Cort GB74JJ - 4-струнный бас для джаза и фьюжн. Пассивная электроника, гриф из венге. Теплый, округлый звук."),

            ItemDescriptionEntity(itemId = 36, description = "Ibanez SR505 - 5-струнный бас с активной электроникой. Узкий гриф, легкий корпус. Расширенный диапазон для современных стилей."),
            ItemDescriptionEntity(itemId = 37, description = "Spector Legend 5 - 5-струнный бас с характерным дизайном. Активная электроника, агрессивный звук для металла."),

            ItemDescriptionEntity(itemId = 38, description = "Schecter Stiletto Studio 6 - 6-струнный бас для сложных партий. Активная электроника, 24 лада. Максимальный диапазон."),
            ItemDescriptionEntity(itemId = 39, description = "Ibanez GSR206 - доступный 6-струнный бас. Пассивные звукосниматели, отличный вариант для изучения многоголосных техник."),

            ItemDescriptionEntity(itemId = 40, description = "Taylor GS Mini-e Bass - компактный акустический бас джамбо. Встроенный звукосниматель, идеален для акустических выступлений."),
            ItemDescriptionEntity(itemId = 41, description = "Fender Kingman Bass - акустический бас с корпусом джамбо. Громкий, насыщенный звук, встроенный тюнер."),

            ItemDescriptionEntity(itemId = 42, description = "Ibanez AEB10E - электроакустический бас дредноут. Встроенный эквалайзер, мощный звук для сцены."),
            ItemDescriptionEntity(itemId = 43, description = "Crafter BA-400 - акустический бас с корпусом дредноут. Качественная сборка, насыщенный звук."),

            ItemDescriptionEntity(itemId = 44, description = "Gibson J-45 Bass - премиальный акустический бас. Корпус из красного дерева, исключительное качество звучания."),
            ItemDescriptionEntity(itemId = 45, description = "Martin BC-15E - акустический бас от легендарного производителя. Корпус из махагони, встроенная электроника."),

            ItemDescriptionEntity(itemId = 46, description = "Русская Балалайка Электро Прима - профессиональный инструмент с пьезозвукоснимателем. Корпус из ели, гриф из бука. Аутентичный звук с возможностью усиления."),
            ItemDescriptionEntity(itemId = 47, description = "Теремок Электро Прима - электронная балалайка для сцены. Встроенный предусилитель, регулировка громкости и тембра."),

            ItemDescriptionEntity(itemId = 48, description = "Русская Балалайка Электро Секунда - балалайка секунда с нижним строем. Идеальна для ансамблевой игры, создает гармоническую основу."),
            ItemDescriptionEntity(itemId = 49, description = "Теремок Электро Секунда - электронная версия балалайки секунда. Удобный корпус, качественная электроника."),

            ItemDescriptionEntity(itemId = 50, description = "Русская Балалайка Электро Альт - альтовая балалайка с теплым тембром. Используется для средних голосов в ансамбле."),
            ItemDescriptionEntity(itemId = 51, description = "Теремок Электро Альт - электронная альтовая балалайка. Усиленный корпус, стойкость к обратной связи."),

            ItemDescriptionEntity(itemId = 52, description = "Русская Балалайка Электро Бас - бас-балалайка с глубоким звучанием. Создает ритмическую и гармоническую основу ансамбля."),
            ItemDescriptionEntity(itemId = 53, description = "Теремок Электро Бас - электронная бас-балалайка. Мощный звук, идеальна для современных аранжировок."),

            ItemDescriptionEntity(itemId = 54, description = "Русская Балалайка Электро Контрабас - самая большая балалайка оркестра. Очень низкий строй, фундаментальный звук."),
            ItemDescriptionEntity(itemId = 55, description = "Теремок Электро Контрабас - электронная контрабас-балалайка. Специальная конструкция для низких частот, встроенный компрессор."),

            ItemDescriptionEntity(itemId = 56, description = "Русская Балалайка Прима - классическая акустическая балалайка. Корпус из ели, гриф из бука. Традиционный русский звук."),
            ItemDescriptionEntity(itemId = 57, description = "Теремок Прима - акустическая балалайка для начинающих. Качественные материалы, доступная цена."),

            ItemDescriptionEntity(itemId = 58, description = "Русская Балалайка Секунда - акустическая балалайка секунда. Низкий строй, используется в оркестрах."),
            ItemDescriptionEntity(itemId = 59, description = "Теремок Секунда - доступная акустическая балалайка секунда. Идеальна для учебных заведений и ансамблей."),

            ItemDescriptionEntity(itemId = 60, description = "Русская Балалайка Альт - акустическая альтовая балалайка. Средний диапазон, теплый тембр."),
            ItemDescriptionEntity(itemId = 61, description = "Теремок Альт - акустическая балалайка альт. Качественная сборка, традиционное звучание."),

            ItemDescriptionEntity(itemId = 62, description = "Русская Балалайка Бас - акустическая бас-балалайка. Большой корпус, глубокий звук."),
            ItemDescriptionEntity(itemId = 63, description = "Теремок Бас - акустическая бас-балалайка. Усиленная конструкция, громкое звучание."),

            ItemDescriptionEntity(itemId = 64, description = "Русская Балалайка Контрабас - акустическая контрабас-балалайка. Самый большой размер, очень низкий звук."),
            ItemDescriptionEntity(itemId = 65, description = "Теремок Контрабас - акустическая контрабас-балалайка. Массивная конструкция, мощный звук."),

            ItemDescriptionEntity(itemId = 66, description = "Kala KA-15S - электронное укулеле сопрано с пьезозвукоснимателем. Компактный размер, яркий звук. Идеально для путешествий."),
            ItemDescriptionEntity(itemId = 67, description = "Mahalo U-30 - доступное электронное укулеле сопрано. Встроенный тюнер, отличный вариант для начинающих."),

            ItemDescriptionEntity(itemId = 68, description = "Cordoba 15CM - электронное укулеле концерт с корпусом из красного дерева. Более глубокий звук чем у сопрано, удобный гриф."),
            ItemDescriptionEntity(itemId = 69, description = "Flight TUC-34 - электронное укулеле концерт с активной электроникой. Регулировка тембра, встроенный клипсовый тюнер."),

            ItemDescriptionEntity(itemId = 70, description = "Kala KA-ATP-CTG - электронное укулеле тенор с кедровым топом. Теплый, объемный звук, расширенный диапазон."),
            ItemDescriptionEntity(itemId = 71, description = "Cordoba 20TM - электронное укулеле тенор с корпусом из махагони. Богатый, сложный тембр, идеален для сольных партий."),

            ItemDescriptionEntity(itemId = 72, description = "Kala KA-ABP-CTG - электронное укулеле баритон. Самый низкий строй среди укулеле, звучит почти как гитара."),
            ItemDescriptionEntity(itemId = 73, description = "Cordoba 24B - электронное укулеле баритон с корпусом из красного дерева. Глубокий, насыщенный звук, уникальный характер."),

            ItemDescriptionEntity(itemId = 74, description = "Flight NUS310 - акустическое укулеле сопрано для начинающих. Нейлоновые струны, легкий корпус. Идеальный первый инструмент."),
            ItemDescriptionEntity(itemId = 75, description = "Hora U-10 - акустическое укулеле сопрано традиционной формы. Качественная сборка, аутентичный гавайский звук."),

            ItemDescriptionEntity(itemId = 76, description = "Kala KA-C - акустическое укулеле концерт с корпусом из красного дерева. Удобный размер, сбалансированный звук."),
            ItemDescriptionEntity(itemId = 77, description = "Mahalo MK2 - акустическое укулеле концерт по доступной цене. Цветные струны, яркий дизайн, веселый звук."),

            ItemDescriptionEntity(itemId = 78, description = "Cordoba 22T - акустическое укулеле тенор премиум-класса. Корпус из цельного кедра, исключительное качество звука."),
            ItemDescriptionEntity(itemId = 79, description = "Flight NUT430 - акустическое укулеле тенор с корпусом из ели. Громкий, проекционный звук, идеален для сцены."),

            ItemDescriptionEntity(itemId = 80, description = "Kala KA-BG - акустическое укулеле баритон. Низкий строй DGBE, как у гитары. Идеален для гитаристов."),
            ItemDescriptionEntity(itemId = 81, description = "Cordoba 24B Акустика - акустическое укулеле баритон с корпусом из красного дерева. Теплый, гитароподобный звук."),

            ItemDescriptionEntity(itemId = 82, description = "Yamaha YEV104 - электроскрипка 4/4 с пятью струнами. Расширенный диапазон, современный дизайн. Идеальна для экспериментальной музыки."),
            ItemDescriptionEntity(itemId = 83, description = "Cecilio CVN-500 - электроскрипка 4/4 по доступной цене. В комплекте наушники, кабель и чехол. Отличный вариант для репетиций."),

            ItemDescriptionEntity(itemId = 84, description = "Stagg EVN - электроскрипка 7/8 для музыкантов с небольшими руками. Удобный размер, качественная электроника."),
            ItemDescriptionEntity(itemId = 85, description = "Barcus Berry - электроскрипка 7/8 премиум-класса. Профессиональный звук, ручная сборка."),

            ItemDescriptionEntity(itemId = 86, description = "NS Design WAV4 - электроскрипка 3/4 с уникальным дизайном. Эргономичный корпус, современный звук."),
            ItemDescriptionEntity(itemId = 87, description = "Zeta Jazz - электроскрипка 3/4 для джазовых музыкантов. Теплый, округлый звук, стильный внешний вид."),

            ItemDescriptionEntity(itemId = 88, description = "Yamaha YEV104 1/2 - электроскрипка для подростков. Качественная сборка, регулируемый подбородник."),
            ItemDescriptionEntity(itemId = 89, description = "Cecilio CVN-500 1/2 - электроскрипка для детей. Полный комплект, безопасные материалы."),

            ItemDescriptionEntity(itemId = 90, description = "Stagg EVN 1/4 - электроскрипка для маленьких детей. Легкий корпус, мягкие струны, удобный размер."),
            ItemDescriptionEntity(itemId = 91, description = "Barcus Berry 1/4 - профессиональная детская электроскрипка. Отличное качество звука, регулируемая подставка."),

            ItemDescriptionEntity(itemId = 92, description = "NS Design WAV4 1/8 - самая маленькая электроскрипка. Для детей от 3 лет, безопасная конструкция."),
            ItemDescriptionEntity(itemId = 93, description = "Zeta Jazz 1/8 - миниатюрная электроскрипка премиум-класса. Идеальна для раннего музыкального развития."),

            ItemDescriptionEntity(itemId = 94, description = "Stradivarius Copy - копия скрипки Страдивари 4/4. Ручная работа, качественные материалы. Богатый, объемный звук."),
            ItemDescriptionEntity(itemId = 95, description = "Mendini MV500 - акустическая скрипка 4/4 для начинающих. Полный комплект: смычок, канифоль, чехол."),

            ItemDescriptionEntity(itemId = 96, description = "Cremona SV-500 - акустическая скрипка 7/8 для взрослых с небольшими руками. Профессиональное качество, ручная настройка."),
            ItemDescriptionEntity(itemId = 97, description = "Stentor 7/8 - акустическая скрипка для студентов. Надежная конструкция, стабильный строй."),

            ItemDescriptionEntity(itemId = 98, description = "Yamaha V3SKA - акустическая скрипка 3/4 для подростков. Качественная сборка, оптимальное соотношение цена/качество."),
            ItemDescriptionEntity(itemId = 99, description = "Cremona SV-175 - акустическая скрипка 3/4 для продвинутых учеников. Улучшенные материалы, лучший звук."),

            ItemDescriptionEntity(itemId = 100, description = "Mendini MV200 - акустическая скрипка 1/2 для детей. Яркий цвет, легкий корпус, удобные колки."),
            ItemDescriptionEntity(itemId = 101, description = "Stentor Student I - акустическая скрипка 1/2 для музыкальных школ. Классическое качество, проверенная временем модель."),

            ItemDescriptionEntity(itemId = 102, description = "Yamaha V5SKA - акустическая скрипка 1/4 для маленьких детей. Безопасные материалы, регулируемый подбородник."),
            ItemDescriptionEntity(itemId = 103, description = "Cremona SV-100 - акустическая скрипка 1/4 начального уровня. Идеальный первый инструмент для ребенка."),

            ItemDescriptionEntity(itemId = 104, description = "Mendini MV50 - акустическая скрипка 1/8 для самых маленьких. Миниатюрный размер, мягкие струны."),
            ItemDescriptionEntity(itemId = 105, description = "Stentor Student I 1/8 - профессиональная детская скрипка. Качественная сборка, хороший звук даже в маленьком размере."),

            ItemDescriptionEntity(itemId = 106, description = "Dunlop Tortex 0.60mm - пластиковые медиаторы средней жесткости. Текстурированная поверхность для надежного хвата. Идеальны для ритм-гитары."),
            ItemDescriptionEntity(itemId = 107, description = "Jim Dunlop Nylon Standard - нейлоновые медиаторы различной толщины. Гибкие, износостойкие, не выскальзывают из пальцев."),

            ItemDescriptionEntity(itemId = 108, description = "Dunlop Jazz III - металлические медиаторы для скоростной игры. Маленький размер, острый кончик. Любимый выбор многих виртуозов."),
            ItemDescriptionEntity(itemId = 109, description = "Planet Waves Black Ice - металлические медиаторы с черным покрытием. Агрессивный атака, яркий звук, стильный внешний вид."),

            ItemDescriptionEntity(itemId = 110, description = "D'Addario EJ27 - нейлоновые струны для классической гитары. Низкое натяжение, мягкое ощущение. Идеальны для начинающих."),
            ItemDescriptionEntity(itemId = 111, description = "Savarez 500AR - профессиональные нейлоновые струны. Высокая четкость, богатый обертонами звук. Для концертных выступлений."),

            ItemDescriptionEntity(itemId = 112, description = "Ernie Ball 2221 - металлические струны Regular Slinky. Сбалансированное натяжение, универсальный звук. Самые популярные струны в мире."),
            ItemDescriptionEntity(itemId = 113, description = "Elixir Nanoweb - металлические струны с нанопокрытием. Долгий срок службы, защита от коррозии. Звучат как новые в несколько раз дольше."),

            ItemDescriptionEntity(itemId = 114, description = "Rotosound RB45 - струны для бас-гитары круглой намоткой. Яркий, атакующий звук с четкими верхами. Классика для рока."),
            ItemDescriptionEntity(itemId = 115, description = "D'Addario EXL170 - струны для бас-гитары Regular Light. Сбалансированный звук, комфортное натяжение. Подходят для большинства стилей."),

            ItemDescriptionEntity(itemId = 116, description = "Первый Струнный - струны для балалайки прима. Традиционные материалы, аутентичный звук. Для классических русских инструментов."),
            ItemDescriptionEntity(itemId = 117, description = "Русские Струны - профессиональные струны для балалайки. Улучшенная стабильность строя, богатый тембр."),

            ItemDescriptionEntity(itemId = 118, description = "Aquila Nylgut - струны для укулеле из синтетического материала. Яркий, проекционный звук, быстро растягиваются. Самые популярные струны для укулеле."),
            ItemDescriptionEntity(itemId = 119, description = "D'Addario EJ87S - струны для укулеле сопрано из фторуглерода. Четкий, сфокусированный звук, долгий срок службы."),

            ItemDescriptionEntity(itemId = 120, description = "Thomastik Dominant - струны для скрипки премиум-класса. Теплый, объемный звук с богатыми обертонами. Стандарт для профессиональных музыкантов."),
            ItemDescriptionEntity(itemId = 121, description = "Pirastro Tonica - струны для скрипки для студентов. Хорошее качество по доступной цене. Стабильный строй, ровный тембр по всем струнам.")
        )

        if (countDescription == 0) descriptionDao.insertDescription(description)
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

    fun aboutButton(view: View) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, AboutFragment())
            .addToBackStack(null)
            .commit()
    }

    fun historyButton(view: View) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, HistoryFragment())
            .addToBackStack(null)
            .commit()
    }
    fun removeFromOrder(view: View) {
        val id = view.tag as Int
        lifecycleScope.launch {
            ordersDao.deleteItem(id, ordersDao.getLastOrderId())

            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
