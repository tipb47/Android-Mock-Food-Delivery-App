package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

class RestaurantImageFragmentDisplay : Fragment() {

    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(ARG_IMAGE_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_image_display, container, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView)

        // use glide to load the image
        imageUrl?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.food_icon)
                .error(R.drawable.ic_launcher_background)
                .into(imageView)
        }

        return view
    }

    companion object {
        private const val ARG_IMAGE_URL = "imageUrl"

        fun newInstance(imageUrl: String) =
            RestaurantImageFragmentDisplay().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URL, imageUrl)
                }
            }
    }
}