package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlacedOrder.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlacedOrder : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_placed_order, container, false)

        viewModel.order.value?.let { displayOrderDetails(view, it) }

        //navigate to restaurant page once selected
        viewModel.navigateToTracker.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_placedOrder_to_mapScreen)
                viewModel.onNavigatedToTracker()
            }
        })

        return view
    }

    //display order details on page
    private fun displayOrderDetails(view: View, order: Order) {
        view.findViewById<TextView>(R.id.tvRestaurantName).text = "Ordered from: ${order.restaurant}"

        val itemsStringBuilder = StringBuilder()
        order.items.forEach { (name, quantity) ->
            itemsStringBuilder.append("$name - Qty: $quantity\n")
        }

        view.findViewById<TextView>(R.id.tvFoodname).text = itemsStringBuilder.toString().trim()
        view.findViewById<TextView>(R.id.tvPrice).text = "Total Price: $${order.price}"
        view.findViewById<TextView>(R.id.tvDate).text = "Date: ${order.date}"
        view.findViewById<TextView>(R.id.tvTime).text = "Time: ${order.time}"
        view.findViewById<TextView>(R.id.tvAddress).text = "Address: ${order.address}"

        // navigate to map
        view.findViewById<Button>(R.id.trackOrderButton).setOnClickListener {
            viewModel.navigateToTracker()
        }
    }
}