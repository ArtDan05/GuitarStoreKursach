package com.example.guitarstore

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DescriptionFragment : Fragment() {
    lateinit var productDao: ProductDao
    lateinit var descriptionDao: ItemDescriptionDao


    companion object {
        private const val ARG_ITEM_ID = "itemId"

        fun newInstance(itemId: Int): DescriptionFragment {
            return DescriptionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_ID, itemId)
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_description_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productDao = DatabaseInstance.db.productDao()
        descriptionDao = DatabaseInstance.db.itemDescriptionDao()

        val itemId = requireArguments().getInt(ARG_ITEM_ID)

        val buyButton: Button = view.findViewById(R.id.buyButtonDesc)
        val productName: TextView = view.findViewById(R.id.productNameDesc)
        val description: TextView = view.findViewById(R.id.DescriptionTextView)
        val price: TextView = view.findViewById(R.id.productPriceDesc)
        val image: ImageView = view.findViewById(R.id.ProductImageDesc)

        lifecycleScope.launch {
            val item = productDao.getProductByID(itemId)
            val itemDescription = descriptionDao.getDescriptionByItemID(itemId)

            productName.text = item.title
            description.text = itemDescription
            price.text = "${if (item.price / 1000 == 0) "" else item.price / 1000} ${String.format("%03d", item.price % 1000)} Руб."
            image.setImageDrawable(getImageFromDB(item.image, image).toDrawable(resources))
        }

        buyButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                ControlHub().addToCart(itemId)
                Toast.makeText(context, "Добавлено в корзину", Toast.LENGTH_LONG).show()
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
}