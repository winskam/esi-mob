package mobg5.g55047.projet.screens.place.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mobg5.g55047.projet.database.FirebasePlaces

class PlaceCreateViewModelFactory(
    private val dataSource: FirebasePlaces.Companion
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceCreateViewModel::class.java)) {
            return PlaceCreateViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
