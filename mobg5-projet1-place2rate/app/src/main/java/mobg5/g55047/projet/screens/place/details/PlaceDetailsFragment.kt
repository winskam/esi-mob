package mobg5.g55047.projet.screens.place.details

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarteist.autoimageslider.SliderView
import mobg5.g55047.projet.LiveDataInternetConnections
import mobg5.g55047.projet.R
import mobg5.g55047.projet.database.FirebasePlaces
import mobg5.g55047.projet.databinding.FragmentPlaceDetailsBinding
import mobg5.g55047.projet.screens.rating.RatingAdapter
import mobg5.g55047.projet.screens.rating.RatingDialogFragment
import java.io.File
import java.util.*

class PlaceDetailsFragment : Fragment() {
    private lateinit var connectionLiveData: LiveDataInternetConnections
    private val FILE_NAME = "place_image"
    private lateinit var photoFile: File
    private lateinit var placeDetailsViewModel: PlaceDetailsViewModel
    private lateinit var placeId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPlaceDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_place_details, container, false
        )
        val arguments = PlaceDetailsFragmentArgs.fromBundle(arguments)
        val viewModelFactory = PlaceDetailsViewModelFactory(
            requireNotNull(this.activity).application,
            FirebasePlaces,
            arguments.placeId
        )
        val placeDetailsViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(PlaceDetailsViewModel::class.java)

        binding.placeDetailsViewModel = placeDetailsViewModel
        this.placeDetailsViewModel = placeDetailsViewModel
        this.placeId = arguments.placeId

        val dialog = RatingDialogFragment(arguments.placeId)

        binding.addRatingDialog.setOnClickListener {
            dialog.show(this.requireActivity().supportFragmentManager, RatingDialogFragment.TAG)
        }

        binding.lifecycleOwner = this

        placeDetailsViewModel.navigateBack.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController().navigate(
                    PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToPlaceFragment()
                )
                placeDetailsViewModel.doneNavigating()
            }
        })

        placeDetailsViewModel.place.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.placeRatingDetails.rating = it.avgRating.toFloat()
                placeDetailsViewModel.getFullAddress()
            }
        })

        val application = requireNotNull(this.activity).application

        connectionLiveData = LiveDataInternetConnections(application)
        connectionLiveData.observe(viewLifecycleOwner, { isConnected ->
            if (isConnected) {
                binding.addImageBtn.visibility = View.VISIBLE
                binding.addRatingDialog.visibility = View.VISIBLE
            } else {
                binding.addImageBtn.visibility = View.INVISIBLE
                binding.addRatingDialog.visibility = View.INVISIBLE
            }
        })

        if (connectionLiveData.value == true) {
            binding.addImageBtn.visibility = View.VISIBLE
            binding.addRatingDialog.visibility = View.VISIBLE
        } else {
            binding.addImageBtn.visibility = View.INVISIBLE
            binding.addRatingDialog.visibility = View.INVISIBLE
        }

        val adapter = RatingAdapter()
        binding.recyclerRatings.adapter = adapter

        placeDetailsViewModel.ratings.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.submitList(it)
            }
        })

        val manager = LinearLayoutManager(context)
        binding.recyclerRatings.layoutManager = manager

        binding.addImageBtn.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getImageFile()
            val fileProvider = FileProvider.getUriForFile(
                requireContext(),
                "mobg5.g55047.projet.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (this.requireActivity().packageManager != null) {
                getImage.launch(takePictureIntent)
            } else {
                Toast.makeText(requireContext(), "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }

        placeDetailsViewModel.images.observe(viewLifecycleOwner, Observer {
            if (it.size != 0) {
                setImageInSlider(it as ArrayList<String>, binding.placeImageDetails)
            }
        })
        return binding.root
    }

    private fun setImageInSlider(images: ArrayList<String>, imageSlider: SliderView) {
        val adapter = SliderImageAdapter()
        adapter.renewItems(images)
        imageSlider.setSliderAdapter(adapter)
        imageSlider.isAutoCycle = true
        imageSlider.startAutoCycle()
    }

    private fun getImageFile(): File {
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(FILE_NAME, ".jpg", storageDir)
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        placeDetailsViewModel.saveImageToDatabase(photoFile.absolutePath, placeId)
    }
}
