package eu.hanna.foodapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.hanna.foodapp.db.MealDatabase
import eu.hanna.foodapp.model.Meal
import eu.hanna.foodapp.model.MealList
import eu.hanna.foodapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    val mealDatabase: MealDatabase
) : ViewModel() {

    private var mealDetailLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id: String) {
        RetrofitInstance.foodApi.getMealById(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]

                    mealDetailLiveData.value = randomMeal

                   // Log.d ("TEST","meal id- ${randomMeal.idMeal} name- ${randomMeal.strMeal}")
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity",t.message.toString())
            }

        })
    }

    fun observeMealDetailLiveData(): LiveData<Meal> {
        return mealDetailLiveData
    }

    // insert the favourite meal to the database
    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }




}