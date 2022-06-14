package eu.hanna.foodapp.retrofit

import eu.hanna.foodapp.model.CategoryList
import eu.hanna.foodapp.model.MealsByCategoryList
import eu.hanna.foodapp.model.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Mealapi {

    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealById(@Query("i") id:String):Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c") popularItem:String):Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories() : Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName:String):Call<MealsByCategoryList>

}