package mobg5.g55047.projet.screens.place.details

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import mobg5.g55047.projet.database.FirebasePlaces
import mobg5.g55047.projet.database.FirebaseUtil
import mobg5.g55047.projet.model.Place
import mobg5.g55047.projet.model.Rating
import java.io.File
import java.util.*

class PlaceDetailsViewModel(
    val application: Application,
    dataSource: FirebasePlaces.Companion,
    val placeId: String
) : ViewModel() {
    val database = dataSource
    var place = MutableLiveData<Place>()
    var ratings = MutableLiveData<List<Rating>>()
    var images: MutableLiveData<MutableList<String>> = MutableLiveData()
    var completeAdress: MutableLiveData<String> = MutableLiveData()
    var visible: MutableLiveData<Boolean> = MutableLiveData()
    private val _navigateBack = MutableLiveData<Boolean?>()
    val navigateBack: LiveData<Boolean?>
        get() = _navigateBack

    init {
        images.value = mutableListOf()
        place = database.getPlace(placeId)
        ratings = database.getRatings(placeId)
        visible.value = FirebaseAuth.getInstance().getCurrentUser() != null
        getListImages()
    }

    fun doneNavigating() {
        _navigateBack.value = null
    }

    fun onClose() {
        _navigateBack.value = true
    }

    fun saveImageToDatabase(path: String, placeId: String) {
        addImage(path, placeId)
    }

    private fun addImage(string: String) {
        images.value!!.add(string)
        images.value = images.value
    }

    private fun addImage(path: String, placeId: String) {
        val file = Uri.fromFile(File(path))
        val storage =
            FirebaseUtil.getFirestorage()!!.reference.storage.getReference(
                "images/${placeId}/${
                    UUID.randomUUID()
                }"
            )
        storage.putFile(file).addOnProgressListener {
        }.addOnCompleteListener {
            File(path).delete()
            getListImages()
        }
    }

    private fun getListImages() {
        images.value = mutableListOf()
        val path = "images/${placeId}/"
        val listRef = FirebaseUtil.getFirestorage()!!.reference.storage.reference.child(path)

        listRef.listAll().addOnSuccessListener {
            for (item in it.items) {
                item.downloadUrl.addOnSuccessListener {
                    addImage(it.toString())
                }
            }
        }
    }

    fun getFullAddress() {
        try {
            val lat = place.value!!.latitude
            val lon = place.value!!.longitude
            val location = Location("")
            location.latitude = lat
            location.longitude = lon
            val addresses: List<Address>
            val geocoder = Geocoder(application.applicationContext, Locale.getDefault())

            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )

            val address: String =
                addresses[0].getAddressLine(0)

            completeAdress.value = address
        } catch (e: Exception) {
            completeAdress.value = place.value!!.city
        }
    }
}
