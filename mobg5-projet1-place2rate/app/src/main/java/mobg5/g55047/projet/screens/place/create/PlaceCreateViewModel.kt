package mobg5.g55047.projet.screens.place.create

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mobg5.g55047.projet.database.FirebasePlaces
import mobg5.g55047.projet.database.FirebaseUtil

class PlaceCreateViewModel(
    dataSource: FirebasePlaces.Companion
) : ViewModel() {
    val database = dataSource
    var placeName = MutableLiveData<String?>()
    var placeCategory = MutableLiveData<String?>()
    var placeCreator: String
    var placeLocation: Location? = null
    var placeCity = MutableLiveData<String?>()
    var address = MutableLiveData<String?>()

    private val _validPlace = MutableLiveData<Boolean?>()
    val validPlace: MutableLiveData<Boolean?>
        get() = _validPlace

    private val _navigateBack = MutableLiveData<Boolean?>()
    val navigateBack: LiveData<Boolean?>
        get() = _navigateBack

    init {
        placeCreator = FirebaseUtil.getAuth()?.uid.toString()
    }

    fun doneNavigating() {
        _navigateBack.value = null
    }

    fun onClose() {
        _navigateBack.value = true
    }

    fun onAdd() {
        if (placeName.value != null && placeLocation != null && placeCity.value != null) {
            database.addPlace(
                placeName.value!!, placeLocation!!.longitude, placeLocation!!.latitude,
                placeCity.value!!, placeCategory.value
            )
            validPlace.value = true
            onClose()
        } else {
            validPlace.value = false
        }
    }
}
