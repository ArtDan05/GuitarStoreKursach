package com.example.guitarstore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {
    private lateinit var list: ListView
    private val dao get() = DatabaseInstance.db.categoryDao()
    var currentParentId: Int = 0

    var parentStack = mutableListOf<Int>()

    companion object {
        fun newInstance(parentId: Int?): CategoryFragment {
            val f = CategoryFragment()
            val args = Bundle()
            args.putInt("parentId", parentId ?: -1)
            f.arguments = args
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list = view.findViewById(R.id.CategoryList)

        val parentIdArg = arguments?.getInt("parentId") ?: -1
        val parentId = if (parentIdArg == -1) null else parentIdArg

        viewLifecycleOwner.lifecycleScope.launch {
            val items = if (parentId == null)
                dao.getRootCategories()
            else
                dao.getChildren(parentId)

            val adapter = object : ArrayAdapter<CategoryEntity>(
                requireContext(),
                R.layout.item_category,
                items
            ) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent)
                    view.findViewById<TextView>(R.id.itemText).text = items[position].title
                    return view
                }
            }

            list.adapter = adapter

            list.setOnItemClickListener { adapter, view, position, id ->
                val item = adapter.getItemAtPosition(position) as CategoryEntity
                openCategory(item.id)
            }

        }
    }

    private fun openCategory(id: Int) {
        parentStack.add(id)
        if (parentStack.size == 3) {
            val fragment = ProductsFragment.newInstance(parentStack)

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        else {
            currentParentId = id
            loadCategories(id)
        }
    }

    private fun loadCategories(parentId: Int?) {
        lifecycleScope.launch {
            val items = if (parentId == null)
                dao.getRootCategories()
            else
                dao.getChildren(parentId)

            val adapter = object : ArrayAdapter<CategoryEntity>(
                requireContext(),
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

            list.adapter = adapter
        }
    }

}
