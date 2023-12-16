package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [RecentOrders.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecentOrders : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recent_orders, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.restaurant_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        //navigate to order details page on click of order
        viewModel.navigateToOrderDetailsScreen.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_recentOrders_to_placedOrder)
                viewModel.onNavigatedToOrderDetailsScreen()
            }
        })

        viewModel.orders.observe(viewLifecycleOwner) { ordersList ->
            recyclerView.adapter = OrderDetailsItemAdapter(ordersList) { order ->
                viewModel.order.value = order
                // place order
                viewModel.placeOrder()

                // get restaurant given order name
                viewModel.restaurantsMap.value?.let { restaurants ->
                    val restaurant = restaurants[order.restaurant]
                    restaurant?.let {
                        // set restaurant as current restaurant
                        viewModel._currentRestaurant.value = it
                        // navigate to the order details screen
                        viewModel.navigateToOrderDetailsScreen()
                    }
                }
            }
        }

        return view
    }
}