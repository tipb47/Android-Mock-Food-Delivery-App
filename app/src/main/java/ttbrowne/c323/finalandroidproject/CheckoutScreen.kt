package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutScreen : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()
    private lateinit var orderItemAdapter: OrderItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_checkout_screen, container, false)

        orderItemAdapter = OrderItemAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.orderItemsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = orderItemAdapter

        setupSwipeToDelete(recyclerView) //swipe to delete setup

        viewModel.currentOrder.observe(viewLifecycleOwner) { orderItems ->
            orderItemAdapter.setOrderItems(orderItems)
        }

        //navigate to restaurant page on modify order
        viewModel.navigateToRestaurant.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_checkoutScreen_to_restaurantScreen)
                viewModel.onNavigatedToRestaurant()
            }
        })

        //navigate to order details page once placed order
        viewModel.navigateToOrderDetailsScreen.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_checkoutScreen_to_placedOrder)
                viewModel.onNavigatedToOrderDetailsScreen()
            }
        })

        val modifyOrderButton = view.findViewById<Button>(R.id.modifyOrderButton)
        modifyOrderButton.setOnClickListener {
            viewModel.navigateToRestaurant()
        }

        val placeOrderButton = view.findViewById<Button>(R.id.placeOrderButton)
        placeOrderButton.setOnClickListener {

            val currentOrderItems = viewModel._currentOrder.value ?: emptyList()
            val itemsMap = currentOrderItems.associate { it.name to it.quantity }
            val totalPrice = currentOrderItems.sumOf { it.price * it.quantity }
            val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))

            viewModel.order.value = Order(
                restaurant = viewModel._currentRestaurant.value?.name ?: "",
                items = itemsMap,
                price = totalPrice.toInt().toString(),
                date = currentDate,
                time = currentTime,
                address = view.findViewById<EditText>(R.id.addressEditText).text.toString(),
                specialInstructions = view.findViewById<EditText>(R.id.specialInstructionsEditText).text.toString()
            )

            viewModel.placeOrder() //place order with given order
            viewModel.navigateToOrderDetailsScreen()
        }

        return view
    }

    //swipe to delete logic
    private fun setupSwipeToDelete(recyclerView: RecyclerView) {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false //not needed
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val updatedList = orderItemAdapter.removeItemAt(position)
                viewModel._currentOrder.value = updatedList
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
