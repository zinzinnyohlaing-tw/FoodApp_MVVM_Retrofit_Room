package eu.hanna.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eu.hanna.foodapp.databinding.MealItemsBinding
import eu.hanna.foodapp.model.MealsByCategory

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>()  {

    private var mealsList = ArrayList<MealsByCategory>()

    fun setMealsList(mealsList: ArrayList<MealsByCategory>) {
        this.mealsList = mealsList
        notifyDataSetChanged()

    }

    inner class CategoryMealsViewHolder(var binding: MealItemsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        return CategoryMealsViewHolder(
            MealItemsBinding.inflate(
                LayoutInflater.from(
                    parent.context )
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb  )
            .into(holder.binding.imgMeal)

        holder.binding.tvmealname.text = mealsList[position].strMeal
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

}