package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ttbrowne.c323.finalandroidproject.databinding.FragmentHomeScreenBottomBinding
import ttbrowne.c323.finalandroidproject.databinding.FragmentHomeScreenTopBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreenTop.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeScreenTop : Fragment() {

    private var _binding: FragmentHomeScreenTopBinding? = null
    private val binding get() = _binding!!
    val viewModel: AppViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeScreenTopBinding.inflate(inflater, container, false)

        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.horizontalRestaurantList.layoutManager = horizontalLayoutManager

        viewModel.restaurantsMap.observe(viewLifecycleOwner) { restaurantsMap ->
            val restaurantsList = restaurantsMap.values.toList()
            val horizontalAdapter =
                HorizontalRestaurantButtonsAdapter(restaurantsList) { restaurant ->
                    viewModel._currentRestaurant.postValue(restaurant) //set current restaurant
                    viewModel.navigateToRestaurant()
                }
            binding.horizontalRestaurantList.adapter = horizontalAdapter
        }

        return binding.root
    }

}