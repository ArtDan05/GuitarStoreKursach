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
import com.example.guitarstore.state.RootCategoryState
import com.example.guitarstore.state.SubCategoryState


class CategoryFragment : Fragment() {
    private lateinit var list: ListView
    private val controlHub = ControlHub()

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

        controlHub.categories.observe(viewLifecycleOwner) { items ->
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
        }

        list.setOnItemClickListener { adapter, _, position, _ ->
            val item = adapter.getItemAtPosition(position) as CategoryEntity
            openCategory(item.id)
        }

        controlHub.categoryState = RootCategoryState()

        viewLifecycleOwner.lifecycleScope.launch {
            controlHub.loadCategories()
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
            controlHub.categoryState = SubCategoryState(id)

            viewLifecycleOwner.lifecycleScope.launch {
                controlHub.loadCategories()
            }

        }
    }

}
