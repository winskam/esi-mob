package mobg5.g55047.projet.screens.place.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mobg5.g55047.projet.database.FirebasePlaces

class PlaceDetailsViewModelFactory(
    private val application: Application,
    private val dataSource: FirebasePlaces.Companion,
    private val placeId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceDetailsViewModel::class.java)) {
            return PlaceDetailsViewModel(application, dataSource, placeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
