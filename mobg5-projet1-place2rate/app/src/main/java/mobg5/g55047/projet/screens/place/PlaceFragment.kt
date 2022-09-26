package mobg5.g55047.projet.screens.place

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import mobg5.g55047.projet.LiveDataInternetConnections
import mobg5.g55047.projet.R
import mobg5.g55047.projet.database.FirebasePlaces
import mobg5.g55047.projet.database.HistoryDatabase
import mobg5.g55047.projet.databinding.FragmentPlaceBinding
import java.util.*

class PlaceFragment : Fragment() {
    private lateinit var connectionLiveData: LiveDataInternetConnections

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentPlaceBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_place, container, false
        )
        val application = requireNotNull(this.activity).application
        val history = HistoryDatabase.getInstance(application).historyDatabaseDao
        // Create an instance of the ViewModel Factory.
        val dataSource = FirebasePlaces.Companion
        val viewModelFactory = PlaceViewModelFactory(dataSource, history)
        // Get a reference to the ViewModel associated with this fragment.
        val placeViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(PlaceViewModel::class.java)

        binding.placeViewModel = placeViewModel

        val adapter = PlaceAdapter(PlaceListener { placeId ->
            placeViewModel.onPlaceDetailsClicked(placeId)
        })

        binding.placesList.adapter = adapter

        placeViewModel.placesQuery.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        placeViewModel.navigateToPlaceCreate.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController().navigate(
                    PlaceFragmentDirections
                        .actionPlaceFragmentToPlaceCreateFragment()
                )
                placeViewModel.onPlaceCreateNavigated()
            }
        })

        placeViewModel.navigateToPlaceDetails.observe(viewLifecycleOwner, {
            if (it != null) {
                this.findNavController().navigate(
                    PlaceFragmentDirections
                        .actionPlaceFragmentToPlaceDetailsFragment(it)
                )
                placeViewModel.onPlaceDetailsNavigated()
            }
        })

        placeViewModel.historyString.observe(viewLifecycleOwner, { history ->
            binding.searchBar.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    history
                )
            )
        })

        binding.searchBar.threshold = 1

        binding.addHistory.setOnClickListener {
            placeViewModel.addToHistory()
        }

        placeViewModel.search.observe(viewLifecycleOwner, {
            placeViewModel.getPlacesQuery(it)
        })

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)

        connectionLiveData = LiveDataInternetConnections(application)
        connectionLiveData.observe(viewLifecycleOwner, { isConnected ->
            if (isConnected) {
                binding.addNewPlaceBtn.visibility = View.VISIBLE
            } else {
                binding.addNewPlaceBtn.visibility = View.INVISIBLE
            }
        })

        if (connectionLiveData.value == true) {
            binding.addNewPlaceBtn.visibility = View.VISIBLE
        } else {
            binding.addNewPlaceBtn.visibility = View.INVISIBLE
        }

        val manager = LinearLayoutManager(activity)
        binding.placesList.layoutManager = manager

        return binding.root
    }
}
