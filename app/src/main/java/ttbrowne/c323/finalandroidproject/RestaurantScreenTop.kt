package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2

/**
 * A simple [Fragment] subclass.
 * Use the [RestaurantScreenTop.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantScreenTop : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_screen_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById<ViewPager2>(R.id.imageViewPager)

        // observe the current restaurant livedata
        viewModel._currentRestaurant.observe(viewLifecycleOwner) { restaurant ->
            restaurant?.images?.let { imageUrls ->
                // initialize the adapter with the image URLs
                val adapter = ImagePagerAdapter(this, imageUrls)
                viewPager.adapter = adapter
            } ?: run {
                // error
            }
        }
    }
}