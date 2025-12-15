package com.example.guitarstore

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    lateinit var orderList: ListView
    var items = mutableListOf<ProductEntity>()
    lateinit var itemIds: List<Int>
    lateinit var ordersDao: OrdersDao
    lateinit var productDao: ProductDao
    lateinit var orderStatusDao: OrdersIsCompletedDao
    lateinit var tView: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items = mutableListOf<ProductEntity>()

        ordersDao = DatabaseInstance.db.ordersDao()
        productDao = DatabaseInstance.db.productDao()
        orderList = view.findViewById<ListView>(R.id.currentOrder)
        orderStatusDao = DatabaseInstance.db.orderIsCompletedDao()
        tView = view.findViewById<TextView>(R.id.tView)

        lifecycleScope.launch {
            if (ordersDao.getLastOrderId() != null) {
                tView.text = "Текущий заказ: ${ordersDao.getLastOrderId()}"
                if (!orderStatusDao.getOrderStatus(ordersDao.getLastOrderId())) {
                    val adapter = object : BaseAdapter() {
                        override fun getCount() = items.size
                        override fun getItem(i: Int) = items[i]
                        override fun getItemId(i: Int) = items[i].id.toLong()

                        override fun getView(i: Int, convertView: View?, parent: ViewGroup): View {
                            val v = convertView ?: layoutInflater.inflate(
                                R.layout.card_in_order, parent, false
                            )

                            val name = v.findViewById<TextView>(R.id.nameOfProductOrd)
                            val price = v.findViewById<TextView>(R.id.productPriceOrd)
                            val img = v.findViewById<ImageView>(R.id.productPhotoOrd)
                            val button = v.findViewById<Button>(R.id.removeFromOrder)

                            val p = items[i]

                            name.text = p.title
                            price.text =
                                "${if (items[i].price / 1000 == 0) "" else items[i].price / 1000} ${
                                    String.format(
                                        "%03d",
                                        items[i].price % 1000
                                    )
                                } Руб."
                            img.setImageDrawable(getImageFromDB(p.image, img).toDrawable(resources))
                            button.tag = p.id

                            return v
                        }
                    }

                    orderList.adapter = adapter
                    updateOrder(adapter)
                }
            }

            else {
                tView.text = "Заказов нет"
            }
        }
    }

    private fun getImageFromDB(byteArray: ByteArray, imageView: ImageView) : Bitmap {
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        imageView.setImageBitmap(
            bmp.scale(512, 512, false)
        )
        return bmp
    }

    private fun updateOrder(adapter: BaseAdapter) {
        lifecycleScope.launch {
            val lastOrderId = ordersDao.getLastOrderId() ?: return@launch
            val itemIds = ordersDao.getOrderById(lastOrderId)

            items.clear()
            for (id in itemIds) {
                items.add(productDao.getProductByID(id))
            }

            adapter.notifyDataSetChanged()
        }
    }

}