package ttbrowne.c323.finalandroidproject

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import ttbrowne.c323.finalandroidproject.databinding.OrderItemCardBinding

class OrderDetailsItemAdapter(
    private var orders: List<Order>,
    private val onTrackOrderClick: (Order) -> Unit // on click
) : RecyclerView.Adapter<OrderDetailsItemAdapter.OrderDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding = OrderItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order, onTrackOrderClick) // pass in click listener
    }

    override fun getItemCount(): Int = orders.size

    class OrderDetailsViewHolder(private val binding: OrderItemCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order, onTrackOrderClick: (Order) -> Unit) {
            binding.tvRestaurantName.text = "Ordered from: ${order.restaurant}"

            // create a string from the items map
            val itemsStringBuilder = StringBuilder()
            order.items.forEach { (name, quantity) ->
                itemsStringBuilder.append("$name - Qty: $quantity\n")
            }

            // set the items text
            binding.tvFoodname.text = itemsStringBuilder.toString().trim()

            binding.tvPrice.text = "Total Price: $${order.price}"
            binding.tvDate.text = "Date: ${order.date}"
            binding.tvTime.text = "Time: ${order.time}"
            binding.tvAddress.text = "Address: ${order.address}"

            binding.trackOrderButton.setOnClickListener {
                onTrackOrderClick(order)
            }
        }
    }
}