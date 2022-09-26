package mobg5.g55047.projet.screens.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import mobg5.g55047.projet.R
import mobg5.g55047.projet.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    companion object {
        const val TAG = "HomeFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    // Get a reference to the ViewModel scoped to this Fragment
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.exploreButton.setOnClickListener {
            this.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToPlaceFragment())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAuthenticationState()
        binding.authButton.setOnClickListener { launchSignInFlow() }
    }

    @SuppressLint("WrongConstant")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in
                Log.i(
                    TAG,
                    "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    /**
     * Observes the authentication state and changes the UI accordingly.
     * If there is a logged in user: show a logout button.
     * If there is no logged in user: show a login button
     */
    private fun observeAuthenticationState() {
        viewModel.authenticationState.observe(viewLifecycleOwner, { authenticationState ->
            when (authenticationState) {
                HomeViewModel.AuthenticationState.AUTHENTICATED -> {
                    binding.authButton.text = getString(R.string.logout_button_text)
                    binding.authButton.setOnClickListener {
                        AuthUI.getInstance().signOut(requireContext())
                    }
                }
                else -> {
                    binding.authButton.text = getString(R.string.login_button_text)
                    binding.authButton.setOnClickListener {
                        launchSignInFlow()
                    }
                }
            }
        })
    }

    private fun launchSignInFlow() {
        // Give users the option to sign in / register with their email
        // they will need to create a password
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        // Create and launch sign-in intent.
        // We listen to the response of this activity with the
        // SIGN_IN_RESULT_CODE code
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                providers
            ).setTheme(R.style.LoginTheme).build(), SIGN_IN_RESULT_CODE
        )
    }
}
