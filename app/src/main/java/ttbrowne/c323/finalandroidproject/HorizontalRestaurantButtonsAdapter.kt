package ttbrowne.c323.finalandroidproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ttbrowne.c323.finalandroidproject.databinding.TopRestaurantsCardBinding

class HorizontalRestaurantButtonsAdapter (
    private val restaurants: List<Restaurant>,
    private val onClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<HorizontalRestaurantButtonsAdapter.HorizontalRestaurantViewHolder>() {

    inner class HorizontalRestaurantViewHolder(val binding: TopRestaurantsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            binding.restaurant = restaurant
            binding.horizontalRestaurantButton.setOnClickListener { onClick(restaurant) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalRestaurantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TopRestaurantsCardBinding.inflate(inflater, parent, false)
        return HorizontalRestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HorizontalRestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
    }

    override fun getItemCount(): Int = restaurants.size
}