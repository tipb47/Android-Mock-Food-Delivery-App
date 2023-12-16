package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ttbrowne.c323.finalandroidproject.databinding.FragmentHomeScreenBinding
import ttbrowne.c323.finalandroidproject.databinding.FragmentRestaurantScreenBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantScreen : Fragment() {

    private var _binding: FragmentRestaurantScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by activityViewModels()

    private lateinit var foodItemAdapter: FoodItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_screen, container, false)

        //navigate to order review page once checkout
        viewModel.navigateToCheckout.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_restaurantScreen_to_checkoutScreen)
                viewModel.onNavigatedToCheckout()
            }
        })

        return view
    }
}