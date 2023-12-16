package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import ttbrowne.c323.finalandroidproject.databinding.FragmentDrawerContainerBinding
import ttbrowne.c323.finalandroidproject.databinding.FragmentSelfieCameraBinding
import ttbrowne.c323.finalandroidproject.databinding.FragmentSignInScreenBinding
import ttbrowne.c323.finalandroidproject.databinding.FragmentSignUpScreenBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DrawerContainer.newInstance] factory method to
 * create an instance of this fragment.
 */
class DrawerContainer : Fragment() {

    private var _binding: FragmentDrawerContainerBinding? = null
    private val binding get() = _binding!!

    val viewModel: AppViewModel by activityViewModels()

    //update user UI on start
    private fun updateUserUI() {
        val headerView = binding.navView.getHeaderView(0)
        val avatarImageView = headerView.findViewById<ImageView>(R.id.header_avatar)
        val nameTextView = headerView.findViewById<TextView>(R.id.header_name)
        val emailTextView = headerView.findViewById<TextView>(R.id.header_email)

        val user = viewModel.user
        nameTextView.text = user.name
        emailTextView.text = user.email

        Glide.with(this).load(user.avatarUri).into(avatarImageView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDrawerContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity() as AppCompatActivity
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        activity.setSupportActionBar(toolbar)

        val drawerLayout = binding.drawerLayout
        val navView = binding.navView

        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val toggle = ActionBarDrawerToggle(
            activity, drawerLayout, toolbar,
            R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //update user stats on drawer
        updateUserUI()

        //navigate when menu buttons clicked
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.HomeTab -> {
                    navController.navigate(R.id.homeScreen)
                }
                R.id.RecentOrdersTab -> {
                    navController.navigate(R.id.recentOrders)
                }
                R.id.CalendarViewTab -> {
                    navController.navigate(R.id.calendarView)
                }
                R.id.SignOutButton -> {
                    viewModel.signOut()
                    view.findNavController().navigate(R.id.action_drawerContainer_to_signUpScreen)
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}