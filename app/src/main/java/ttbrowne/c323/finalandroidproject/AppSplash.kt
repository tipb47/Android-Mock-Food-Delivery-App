package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

class AppSplash : Fragment() {

    val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_app_splash, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({

            val currentUser = viewModel.getCurrentUser()
            if (currentUser != null) {
                // user is signed in
                findNavController().navigate(R.id.action_appSplash_to_drawerContainer)
            } else {
                // no user is signed in, navigate to the sign-up screen
                findNavController().navigate(R.id.action_appSplash_to_signUpScreen2)
            }
        }, 2000)
    }
}