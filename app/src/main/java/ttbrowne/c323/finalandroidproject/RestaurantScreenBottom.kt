package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantScreenBottom.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantScreenBottom : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_screen_bottom, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.food_menu_recyclerview)

        viewModel._currentRestaurant.observe(viewLifecycleOwner) { restaurant ->
            restaurant?.food?.let { foodNames ->
                val foodItems = foodNames.mapIndexed { index, foodName ->
                    FoodItem(foodName, getPriceForItem(index))
                }
                val adapter = FoodItemAdapter(foodItems, { totalPrice ->
                    //update price if needed
                }) {
                    // update the current order in the ViewModel
                    viewModel._currentOrder.value = foodItems.filter { it.quantity > 0 }
                }
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(context)
            }
        }

        val checkoutButton = view.findViewById<Button>(R.id.checkoutButton)
        checkoutButton.setOnClickListener {
            viewModel.navigateToCheckout() //navigate to checkout now
        }

        return view
    }

    private fun getPriceForItem(index: Int): Double {
        return 10.0 + index * 2.0 // pricing logic
    }
}

