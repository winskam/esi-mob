package mobg5.g55047.projet.screens.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import mobg5.g55047.projet.R
import mobg5.g55047.projet.database.UserDatabase
import mobg5.g55047.projet.databinding.FragmentConnectionBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ConnectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConnectionFragment : Fragment() {
    private lateinit var viewModel: ConnectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentConnectionBinding>(
            inflater, R.layout.fragment_connection, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = UserDatabase.getInstance(application).userDatabaseDao
        val viewModelFactory = ConnectionViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ConnectionViewModel::class.java)
        binding.connectionViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.validEmail.observe(viewLifecycleOwner, Observer { valid ->
            valid?.let {
                if (viewModel.validEmail.value == true) {
                    showToast("Valid")
                } else {
                    showToast("Invalid")
                }
            }
        })
        viewModel.usersString.observe(viewLifecycleOwner, Observer { users ->
            binding.enterEmail.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    users
                )
            )
        })
        return binding.root
    }

    private fun showToast(textToast: String) {
        Toast.makeText(context, textToast, Toast.LENGTH_SHORT).show()
    }
}
