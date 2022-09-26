package mobg5.g55047.projet.screens.place.create

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import mobg5.g55047.projet.R
import mobg5.g55047.projet.database.FirebasePlaces
import mobg5.g55047.projet.databinding.FragmentPlaceCreationBinding
import java.util.*

class PlaceCreateFragment : Fragment() {
    // declare a global variable of FusedLocationProviderClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val LOCATION_PERMISSION_REQ_CODE = 1000

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // in onCreate() initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
        val binding: FragmentPlaceCreationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_place_creation, container, false
        )
        val viewModelFactory = PlaceCreateViewModelFactory(FirebasePlaces)
        val categories = resources.getStringArray(R.array.Categories)
        val placeCreateViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(PlaceCreateViewModel::class.java)

        binding.placeCreateViewModel = placeCreateViewModel
        binding.lifecycleOwner = this

        placeCreateViewModel.navigateBack.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController().navigate(
                    PlaceCreateFragmentDirections.actionPlaceCreateFragmentToPlaceFragment()
                )
                placeCreateViewModel.doneNavigating()
            }
        })

        placeCreateViewModel.validPlace.observe(
            viewLifecycleOwner,
            { status ->
                status?.let {
                    if (placeCreateViewModel.validPlace.value == true) {
                        showToast("Place created !")
                    } else {
                        showToast("Invalid field(s)")
                    }
                    placeCreateViewModel.validPlace.value = null
                }
            })

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, categories
        )
        binding.categoryChoice.adapter = adapter
        binding.categoryChoice.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    placeCreateViewModel.placeCategory.value = adapter.getItem(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // ("Not implemented")
                }
            }
        binding.addLocalisationBtn.setOnClickListener {
            getLastKnownLocation(placeCreateViewModel)
        }
        return binding.root
    }

    fun getLastKnownLocation(placeCreateViewModel: PlaceCreateViewModel) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // request permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_PERMISSION_REQ_CODE
            )
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val addresses: List<Address>
                    val geocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())
                    addresses = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )
                    val address: String =
                        addresses[0].getAddressLine(0)
                    val city: String = addresses[0].locality
                    placeCreateViewModel.address.value = address
                    placeCreateViewModel.placeCity.value = city
                    placeCreateViewModel.placeLocation = location
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission granted
                } else {
                    // permission denied
                    showToast("You need to grant permission to access location")
                }
            }
        }
    }

    private fun showToast(textToast: String) {
        Toast.makeText(context, textToast, Toast.LENGTH_SHORT).show()
    }
}
