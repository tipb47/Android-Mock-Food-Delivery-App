package ttbrowne.c323.finalandroidproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodItemAdapter(
    private val foodItems: List<FoodItem>,
    private val onTotalPriceChanged: (Double) -> Unit,
    private val onOrderChanged: () -> Unit // callback for when the order changes
) : RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder>() {

    //add each menu item to recycler
    class FoodItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFoodName: TextView = view.findViewById(R.id.tvFoodName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val btnDecrease: Button = view.findViewById(R.id.btnDecrease)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val btnIncrease: Button = view.findViewById(R.id.btnIncrease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item_card, parent, false)
        return FoodItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val foodItem = foodItems[position]
        holder.tvFoodName.text = foodItem.name
        holder.tvPrice.text = String.format("$%.2f", foodItem.price)
        holder.tvQuantity.text = foodItem.quantity.toString()

        val updateViewsAndNotifyChange = {
            holder.tvQuantity.text = foodItem.quantity.toString()
            updateTotalPrice()
            onOrderChanged() // notify that the order has changed
        }

        holder.btnIncrease.setOnClickListener {
            foodItem.quantity++
            updateViewsAndNotifyChange()
        }

        holder.btnDecrease.setOnClickListener {
            if (foodItem.quantity > 0) {
                foodItem.quantity--
                updateViewsAndNotifyChange()
            }
        }
    }

    override fun getItemCount() = foodItems.size

    private fun updateTotalPrice() {
        val totalPrice = foodItems.sumOf { it.price * it.quantity }
        onTotalPriceChanged(totalPrice)
    }

    fun getCurrentFoodItems(): List<FoodItem> {
        return foodItems.filter { it.quantity > 0 }
    }
}
