package mobg5.g55047.projet.screens.rating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mobg5.g55047.projet.database.FirebasePlaces

class RatingViewModelFactory(
    private val dataSource: FirebasePlaces.Companion,
    private val placeId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RatingViewModel::class.java)) {
            return RatingViewModel(dataSource, placeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
