package eu.hanna.foodapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import eu.hanna.foodapp.R
import eu.hanna.foodapp.databinding.ActivityMainBinding
import eu.hanna.foodapp.databinding.ActivityMealBinding
import eu.hanna.foodapp.db.MealDatabase
import eu.hanna.foodapp.fragments.HomeFragment
import eu.hanna.foodapp.model.Meal
import eu.hanna.foodapp.viewModel.HomeViewModel
import eu.hanna.foodapp.viewModel.MealViewModel
import eu.hanna.foodapp.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    // create a binding instance
    private lateinit var binding: ActivityMealBinding

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String

    private lateinit var mealMvvm: MealViewModel
    private lateinit var mealToSave: Meal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]
        //mealMvvm = ViewModelProviders.of(this)[MealViewModel::class.java]


        getMealInformationFromIntent()

        setInformationInView()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()
        onYoutubeImageClick()

        onFavouriteInsertClick()
    }

    private fun onFavouriteInsertClick() {
        binding.btnAddToFav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Meal Saved",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }


    // Get the random meal information from home fragment to meal activity
    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    // set the random meal information to the view
    private fun setInformationInView () {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    // observe or get random meal details from the home activity by ID
    //private var mealToSave:Meal?=null
    private fun observerMealDetailsLiveData(){
        mealMvvm.observeMealDetailLiveData().observe(this,
            { t ->

                // observe the livedata response from the api
                onResponseCase()

                val meal = t
                mealToSave = meal
                binding.tvCategoryInfo.text = "Category : ${meal!!.strCategory}"
                binding.tvLocationInfo.text = "Location : ${meal!!.strArea}"
                binding.tvContent.text = meal.strInstructions
                youtubeLink = meal.strYoutube.toString()
            })
    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategoryInfo.visibility = View.INVISIBLE
        binding.tvLocationInfo.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategoryInfo.visibility = View.VISIBLE
        binding.tvLocationInfo.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}