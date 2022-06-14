package eu.hanna.foodapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import eu.hanna.foodapp.R
import eu.hanna.foodapp.adapters.CategoriesAdapter
import eu.hanna.foodapp.adapters.CategoryMealsAdapter
import eu.hanna.foodapp.databinding.ActivityCategoryMealsBinding
import eu.hanna.foodapp.fragments.HomeFragment
import eu.hanna.foodapp.model.MealsByCategory
import eu.hanna.foodapp.viewModel.CategoryMealsViewModel
import eu.hanna.foodapp.viewModel.MealViewModel

class CategoryMealsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter

    private lateinit var categoryName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]

        categoryMealsViewModel.getMealByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.observeMealsLiveData().observe(this,{
            mealList ->
          //  binding.tvCategoryCount.text = categoryMealsViewModel.getMealByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
            categoryMealsAdapter.setMealsList(mealsList = mealList as ArrayList<MealsByCategory>)
        })


    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoryMealsAdapter
        }
    }


}