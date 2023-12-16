package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ttbrowne.c323.finalandroidproject.databinding.FragmentHomeScreenBottomBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreenBottom.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeScreenBottom : Fragment() {

    private var _binding: FragmentHomeScreenBottomBinding? = null
    private val binding get() = _binding!!
    val viewModel: AppViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeScreenBottomBinding.inflate(inflater, container, false)

        binding.restaurantList.layoutManager = LinearLayoutManager(context)

        viewModel.restaurantsMap.observe(viewLifecycleOwner, Observer { restaurantsMap ->
            val restaurantsList = restaurantsMap.values.toList()
            val adapter = VerticalRestaurantButtonsAdapter(restaurantsList) { restaurant ->
                viewModel._currentRestaurant.postValue(restaurant) //set current restaurant
                viewModel.navigateToRestaurant()
            }
            binding.restaurantList.adapter = adapter
        })

        return binding.root
    }
}