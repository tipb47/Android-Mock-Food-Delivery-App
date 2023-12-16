package ttbrowne.c323.finalandroidproject

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ImagePagerAdapter(
    fragment: Fragment,
    private val imageUrls: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = imageUrls.size

    //setup 3 images to swipe through
    override fun createFragment(position: Int): Fragment {
        return RestaurantImageFragmentDisplay.newInstance(imageUrls[position])
    }
}