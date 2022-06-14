package eu.hanna.foodapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import eu.hanna.foodapp.R
import eu.hanna.foodapp.activities.MainActivity
import eu.hanna.foodapp.adapters.FavouriteMealsAdapter
import eu.hanna.foodapp.databinding.FragmentFavouriteBinding
import eu.hanna.foodapp.viewModel.HomeViewModel


class FavouriteFragment : Fragment() {
    private lateinit var binding:FragmentFavouriteBinding
    private lateinit var viewModel:HomeViewModel

    // create instance of FavouriteMealsAdapter
    private lateinit var favouriteAdapter:FavouriteMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        // observe the favourite meal data from random meals
        observeFavouriteMeals()

        // A function to swipe to delete the favourite items at Favourite Recyclerview
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val position = viewHolder.adapterPosition
                viewModel.deleteMeal(favouriteAdapter.differ.currentList[position])

                Snackbar.make(requireView(),"Meal deleted",Snackbar.LENGTH_SHORT).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.insertMeal(favouriteAdapter.differ.currentList[position])
                    }
                ).show()
            }

        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavourites)
    }

    private fun prepareRecyclerView() {
        favouriteAdapter = FavouriteMealsAdapter()
        binding.rvFavourites.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = favouriteAdapter
        }
    }

    private fun observeFavouriteMeals() {
        viewModel.observeFavouritesMealsLiveData().observe(viewLifecycleOwner,{favouritemeal ->
            favouriteAdapter.differ.submitList(favouritemeal)

            favouritemeal.forEach {
                Log.d("test",it.idMeal)
            }
        })
    }
}