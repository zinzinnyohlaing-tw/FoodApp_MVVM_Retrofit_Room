package eu.hanna.foodapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import eu.hanna.foodapp.R
import eu.hanna.foodapp.activities.MainActivity
import eu.hanna.foodapp.adapters.CategoriesFragmentAdapter
import eu.hanna.foodapp.databinding.FragmentCategoryBinding
import eu.hanna.foodapp.model.Category
import eu.hanna.foodapp.viewModel.CategoryMealsViewModel
import eu.hanna.foodapp.viewModel.HomeViewModel


class CategoryFragment : Fragment() {

    private lateinit var binding:FragmentCategoryBinding
    private lateinit var categoriesFragmentAdapter: CategoriesFragmentAdapter
    private lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // viewModel = ViewModelProviders.of(this)[HomeViewModel::class.java]
        viewModel = (activity as MainActivity).viewModel
        categoriesFragmentAdapter = CategoriesFragmentAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoryLiveData()
    }

    private fun observeCategoryLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner,{
            categories ->
            categoriesFragmentAdapter.setCategoriesList(categoryList = categories as ArrayList<Category>)
        })
    }

    private fun prepareCategoriesRecyclerView() {
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
            adapter = categoriesFragmentAdapter
        }
    }
}