package eu.hanna.foodapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import eu.hanna.foodapp.activities.CategoryMealsActivity
import eu.hanna.foodapp.activities.MainActivity
import eu.hanna.foodapp.activities.MealActivity
import eu.hanna.foodapp.adapters.CategoriesAdapter
import eu.hanna.foodapp.adapters.MostPopularAdapter
import eu.hanna.foodapp.databinding.FragmentHomeBinding
import eu.hanna.foodapp.model.Category
import eu.hanna.foodapp.model.MealsByCategory
import eu.hanna.foodapp.model.Meal
import eu.hanna.foodapp.viewModel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    // create instance for Meal data class
    private lateinit var randomMeal: Meal

    // create instance for adapter for MostPopularAdapter
    private lateinit var popularItemsAdapter:MostPopularAdapter

    // create instance for adapter for CategoriesAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "eu.hanna.foodapp.fragments.idMeal"
        const val MEAL_NAME = "eu.hanna.foodapp.fragments.nameMeal"
        const val MEAL_THUMB = "eu.hanna.foodapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "eu.hanna.foodapp.fragments.categoryname"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //homeMvvm = ViewModelProviders.of(this)[HomeViewModel::class.java]

        // Now the viewmodel is equal the viewmodel inside the main activity
        viewModel = (activity as MainActivity).viewModel

        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemLiveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()

        onItemsCategoryClick()

    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun onItemsCategoryClick() {
       categoriesAdapter.onItemClick = { category ->
           val intent = Intent(activity,CategoryMealsActivity::class.java)
           intent.putExtra(CATEGORY_NAME,category.strCategory)
           startActivity(intent)
       }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {

            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemsAdapter
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.recyclerView.apply {
           layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    private fun observePopularItemLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner,
            { mealList->

                // Prepare the Recycler view
                popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
            })
    }

    private fun observerRandomMeal(){
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner,
            { meal ->

                // Get the image from the api strMealThumb
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)

                this.randomMeal = meal
            })
    }

    // observe the meals by categories
    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner,{ categories ->
            categoriesAdapter.setCategoriesList(categoryList = categories as ArrayList<Category>)
        })
    }

    // Function call for MealActivity
    private fun onRandomMealClick() {
        binding.imgRandomMeal.setOnClickListener {
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }
}