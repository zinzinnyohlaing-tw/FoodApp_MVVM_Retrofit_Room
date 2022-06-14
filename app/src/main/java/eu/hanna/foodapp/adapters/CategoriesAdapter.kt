package eu.hanna.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eu.hanna.foodapp.databinding.CategoryItemBinding
import eu.hanna.foodapp.databinding.PopularItemsBinding
import eu.hanna.foodapp.model.Category
import eu.hanna.foodapp.model.MealsByCategory

class CategoriesAdapter(): RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var categoryList = ArrayList<Category>()
    lateinit var onItemClick:((Category) -> Unit)

    fun setCategoriesList(categoryList: ArrayList<Category>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(var binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(
                    parent.context )
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoryList[position].strCategoryThumb)
            .into(holder.binding.imgCategory)

        holder.binding.categoryName.text = categoryList[position].strCategory

        // create clickListener on items at the category list
        holder.itemView.setOnClickListener {
            onItemClick.invoke(categoryList[position])
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}