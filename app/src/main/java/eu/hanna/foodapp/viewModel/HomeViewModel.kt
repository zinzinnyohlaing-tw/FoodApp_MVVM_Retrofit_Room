package eu.hanna.foodapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.hanna.foodapp.db.MealDatabase
import eu.hanna.foodapp.model.*
import eu.hanna.foodapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase:MealDatabase
) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favouriteMealsLiveData = mealDatabase.mealDao().getAllMeals()

    private var saveSateRandomMeal: Meal ?= null
    // Get the random meal to show on imageview at HomeFragment
    fun getRandomMeal() {

        // save the state inside the randomMealLiveData incase if the random meal is not null
        saveSateRandomMeal?.let { randomMeal ->
            randomMealLiveData.postValue(randomMeal)
            return

        }
        RetrofitInstance.foodApi.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]

                   randomMealLiveData.value = randomMeal
                   // saveSateRandomMeal = randomMeal

                    Log.d ("TEST","meal id- ${randomMeal.idMeal} name- ${randomMeal.strMeal}")
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Home Fragment",t.message.toString())
            }

        })
    }

    // Get the meals by categories
    fun getCategories() {
        RetrofitInstance.foodApi.getCategories().enqueue(object :Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
               if(response.body()!=null){
                   categoriesLiveData.value = response.body()!!.categories
               }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("Home Fragment",t.message.toString())
            }

        })
    }

    // Get the popular items to show on recyclerview at HomeFragment
    fun getPopularItems() {
        RetrofitInstance.foodApi.getPopularItems("Seafood").enqueue(object:Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body() != null) {
                    popularItemsLiveData.value = response.body()!!.meals

                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("Home Fragment",t.message.toString())
            }

        })
    }



    fun observeRandomMealLiveData(): LiveData<Meal>{
        return randomMealLiveData
    }

    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>>{
        return categoriesLiveData
    }

    fun observeFavouritesMealsLiveData(): LiveData<List<Meal>>{
        return favouriteMealsLiveData
    }

    // delete the favourite meal to the database
    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    // insert the favourite meal to the database
    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }
}