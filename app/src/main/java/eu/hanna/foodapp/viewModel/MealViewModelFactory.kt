package eu.hanna.foodapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.hanna.foodapp.db.MealDatabase

class MealViewModelFactory (
    private val mealDatabase: MealDatabase
 ) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(mealDatabase) as T
    }

}