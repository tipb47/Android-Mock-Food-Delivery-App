package ttbrowne.c323.finalandroidproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ttbrowne.c323.finalandroidproject.databinding.BottomRestaurantsCardBinding

class VerticalRestaurantButtonsAdapter (
    private val restaurants: List<Restaurant>,
    private val onClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<VerticalRestaurantButtonsAdapter.RestaurantViewHolder>() {

    inner class RestaurantViewHolder(val binding: BottomRestaurantsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            binding.restaurant = restaurant
            binding.favRestaurantButton.setOnClickListener { onClick(restaurant) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BottomRestaurantsCardBinding.inflate(inflater, parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
    }

    override fun getItemCount(): Int = restaurants.size
}