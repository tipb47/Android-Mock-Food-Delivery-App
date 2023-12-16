package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ttbrowne.c323.finalandroidproject.databinding.FragmentSignInScreenBinding

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class SignInScreen : Fragment() {
    val TAG = "SignInFragment"
    private var _binding: FragmentSignInScreenBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel : AppViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //navigate to home after sign in
        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signInScreen_to_drawerContainer)
                viewModel.onNavigatedToHome()
            }
        })

        //navigate to sign up
        viewModel.navigateToSignUp.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signInScreen_to_signUpScreen)
                viewModel.onNavigatedToSignUp()
            }
        })

        viewModel.errorHappened.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        })

        binding.buttonSignUp.setOnClickListener {
            viewModel.navigateToSignUp()
        }

        //click listener for sign in button
        binding.buttonSignIn.setOnClickListener {
            viewModel.signIn(binding.editTextEmail.text.toString(),binding.editTextPassword.text.toString())
        }

        return view
    }

}