package com.example.guitarstore

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {
    lateinit var orderList: OrdersDao
    lateinit var dao: ProductDao
    lateinit var itemIds: List<Int>
    var items = mutableListOf<ProductEntity>()
    lateinit var listItems: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = DatabaseInstance.db.productDao()
        orderList = DatabaseInstance.db.ordersDao()
        listItems = view.findViewById<ListView>(R.id.historyView)

        lifecycleScope.launch {
            itemIds = orderList.getAllProducts()
            items.clear()
            for (i in itemIds) {
                items.add(dao.getProductByID(i))
            }


            val adapter = object : BaseAdapter() {
                override fun getCount(): Int = items.size
                override fun getItem(position: Int): Any? = items[position]
                override fun getItemId(position: Int): Long = items[position].id.toLong()

                override fun getView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup?
                ): View? {
                    val view = convertView ?: layoutInflater.inflate(
                        R.layout.card_in_history,
                        parent,
                        false
                    )
                    val price = view.findViewById<TextView>(R.id.productPriceHis)
                    val name = view.findViewById<TextView>(R.id.nameOfProductHis)
                    val image = view.findViewById<ImageView>(R.id.productPhotoHis)

                    price.text =
                        "${if (items[position].price / 1000 == 0) "" else items[position].price / 1000} ${
                            String.format(
                                "%03d",
                                items[position].price % 1000
                            )
                        } Руб."
                    name.text = items[position].title
                    image.setImageDrawable(
                        getImageFromDB(items[position].image, image).toDrawable(
                            resources
                        )
                    )

                    return view
                }
            }

            listItems.adapter = adapter

        }
    }

    private fun getImageFromDB(byteArray: ByteArray, imageView: ImageView) : Bitmap {
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        imageView.setImageBitmap(
            bmp.scale(512, 512, false)
        )
        return bmp
    }
}