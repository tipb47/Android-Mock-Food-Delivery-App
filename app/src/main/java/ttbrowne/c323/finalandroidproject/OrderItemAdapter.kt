package ttbrowne.c323.finalandroidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderItemAdapter : RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>() {


    private var orderItems: MutableList<FoodItem> = mutableListOf()

    fun setOrderItems(items: List<FoodItem>) {
        this.orderItems = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.checkout_food_item_card, parent, false)
        return OrderItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val item = orderItems[position]
        holder.bind(item)
    }

    fun removeItemAt(position: Int): List<FoodItem> {
        orderItems.removeAt(position)
        notifyItemRemoved(position)
        return orderItems.toList()
    }

    override fun getItemCount() = orderItems.size

    class OrderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvFoodName)
        private val quantityTextView: TextView = itemView.findViewById(R.id.tvQuantity)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvPrice)

        fun bind(foodItem: FoodItem) {
            nameTextView.text = foodItem.name
            quantityTextView.text = "Quantity: ${foodItem.quantity}"
            priceTextView.text = "$${foodItem.price.toInt()}"
        }
    }
}