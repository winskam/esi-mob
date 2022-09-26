package mobg5.g55047.projet.screens.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mobg5.g55047.projet.database.FirebasePlaces
import mobg5.g55047.projet.database.HistoryDatabaseDao

class PlaceViewModelFactory(
    private val dataSource: FirebasePlaces.Companion,
    private val databaseHistory: HistoryDatabaseDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
            return PlaceViewModel(dataSource, databaseHistory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
