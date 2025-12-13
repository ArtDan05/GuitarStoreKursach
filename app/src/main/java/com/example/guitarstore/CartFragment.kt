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
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment

class CartFragment : Fragment() {
    private lateinit var cartListView: ListView
    private lateinit var buyButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.cart_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartListView = view.findViewById(R.id.productsInCartView)
        buyButton = view.findViewById(R.id.buyButton)


        val items = CartManager.items

        val adapter = object : BaseAdapter() {
            override fun getCount() = items.size
            override fun getItem(i: Int) = items[i]
            override fun getItemId(i: Int) = items[i].id.toLong()

            override fun getView(i: Int, convertView: View?, parent: ViewGroup): View {
                val v = convertView ?: layoutInflater.inflate(
                    R.layout.card_item_layout, parent, false
                )

                val name = v.findViewById<TextView>(R.id.nameOfProduct)
                val price = v.findViewById<TextView>(R.id.productPrice)
                val img = v.findViewById<ImageView>(R.id.productPhoto)
                val button = v.findViewById<Button>(R.id.removeFromCart)

                val p = items[i]

                name.text = p.title
                price.text = "${if (items[i].price / 1000 == 0) "" else items[i].price / 1000} ${String.format("%03d", items[i].price % 1000)} Руб."
                img.setImageDrawable(getImageFromDB(p.image, img).toDrawable(resources))
                button.tag = p.id

                return v
            }


        }

        cartListView.adapter = adapter

        buyButton.setOnClickListener {
            Toast.makeText(context, "Покупка оформлена!", Toast.LENGTH_SHORT).show()
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