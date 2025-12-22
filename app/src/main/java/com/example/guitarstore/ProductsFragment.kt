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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ProductsFragment : Fragment() {
    private val dao get() = DatabaseInstance.db.productDao()
    private lateinit var list: ListView

    var parentStack = mutableListOf<Int>()

    companion object {
        private const val ARG_PARENT_STACK = "parentStack"

        fun newInstance(stack: List<Int>): ProductsFragment {
            return ProductsFragment().apply {
                arguments = Bundle().apply {
                    putIntegerArrayList(ARG_PARENT_STACK, ArrayList(stack))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentStack = requireArguments()
            .getIntegerArrayList("parentStack")
            ?: error("parentStack is missing")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list = view.findViewById(R.id.productsListView)

        viewLifecycleOwner.lifecycleScope.launch {
            val items = dao.getProductsByCategory(parentStack[0]!!, parentStack[1]!!, parentStack[2]!!)

            list.adapter = object : BaseAdapter() {
                override fun getCount() = items.size
                override fun getItem(position: Int) = items[position]
                override fun getItemId(position: Int) = items[position].id.toLong()

                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = convertView ?: layoutInflater.inflate(R.layout.product_card, parent, false)

                    val nameView = view.findViewById<TextView>(R.id.nameOfProduct)
                    val priceView = view.findViewById<TextView>(R.id.productPrice)
                    val imageView = view.findViewById<ImageView>(R.id.productPhoto)
                    val buyButton = view.findViewById<Button>(R.id.buyButton)

                    nameView.text = items[position].title
                    priceView.text = "${if (items[position].price / 1000 == 0) "" else items[position].price / 1000} ${String.format("%03d", items[position].price % 1000)} Руб."
                    imageView.setImageDrawable(getImageFromDB(items[position].image, imageView).toDrawable(resources))

                    buyButton.setOnClickListener {
                        viewLifecycleOwner.lifecycleScope.launch {
                            ControlHub().addToCart(items[position].id)
                            Toast.makeText(context, "Добавлено в корзину", Toast.LENGTH_LONG).show()
                        }
                    }
                    return view
                }
            }
        }

        list.setOnItemClickListener { adapter, view, position, id ->
            val item = adapter.getItemAtPosition(position) as ProductEntity

            childFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, DescriptionFragment.newInstance(item.id))
                .addToBackStack(null)
                .commit()
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