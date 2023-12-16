package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import ttbrowne.c323.finalandroidproject.databinding.FragmentSignUpScreenBinding

class SignUpScreen : Fragment() {
    private var _binding: FragmentSignUpScreenBinding? = null
    private val binding get() = _binding!!

    val viewModel: AppViewModel by activityViewModels()

    private lateinit var getContent: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel : AppViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Initialize the activity result launcher
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            // Handle the returned Uri
            if (uri != null) {
                binding.imageViewProfile.setImageURI(uri) //update picture
                viewModel.updateAvatarUri(uri)
            }
        }

        viewModel.avatarUri.observe(viewLifecycleOwner, Observer { uri ->
            if (uri != null) {
                binding.imageViewProfile.setImageURI(uri)
            }
        })

        viewModel.errorHappened.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        })

        //navigate to selfie if option chosen
        viewModel.navigateToSelfie.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signUpScreen_to_selfieCameraFragment)
                viewModel.onSelfieNavigated()
            }
        })

        //navigate to home after sign up
        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signUpScreen_to_drawerContainer)
                viewModel.onNavigatedToHome()
            }
        })

        //navigate to sign in
        viewModel.navigateToSignIn.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signUpScreen_to_signInScreen)
                viewModel.onNavigatedToSignIn()
            }
        })

        binding.imageViewProfile.setOnClickListener {
            // show the AvatarPopup
            AvatarPopup(
                onCameraPressed = {
                    viewModel.takeAvatarSelfie()
                },
                onGalleryPressed = {
                    getContent.launch("image/*")
                }
            ).show(parentFragmentManager, AvatarPopup.TAG)
        }

        binding.buttonSignIn.setOnClickListener {
            viewModel.navigateToSignIn()
        }

        binding.buttonSignUp.setOnClickListener {
            viewModel.signUp(binding.editTextEmail.text.toString(),binding.editTextPassword.text.toString(),binding.editTextReEnterPassword.text.toString(),binding.editTextName.text.toString())
        }


        return view
    }
}