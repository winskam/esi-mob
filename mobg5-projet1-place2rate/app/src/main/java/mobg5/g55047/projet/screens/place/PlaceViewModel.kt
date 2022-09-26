package mobg5.g55047.projet.screens.place

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import mobg5.g55047.projet.database.FirebasePlaces
import mobg5.g55047.projet.model.History
import mobg5.g55047.projet.database.HistoryDatabaseDao
import mobg5.g55047.projet.model.Place

class PlaceViewModel(
    dataSource: FirebasePlaces.Companion,
    private val databaseHistory: HistoryDatabaseDao,
) : ViewModel() {
    val database = dataSource
    private val history = databaseHistory.getHistory()
    val historyString = Transformations.map(history) { history ->
        history.map { history -> history.search }
    }
    var search: MutableLiveData<String> = MutableLiveData()
    var places: MutableLiveData<List<Place>> = MutableLiveData()
    var placesQuery: MutableLiveData<List<Place>> = MutableLiveData()
    var visible: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getPlaces()
        visible.value = FirebaseAuth.getInstance().getCurrentUser() != null
    }

    private fun getPlaces() {
        places = database.getPlaces() as MutableLiveData<List<Place>>
        placesQuery = database.getPlaces() as MutableLiveData<List<Place>>
    }

    fun getPlacesQuery(string: String) {
        placesQuery.value = places.value?.filter { place ->
            (place.name?.lowercase()
                ?.contains(string.lowercase()) ?: true || place.city?.lowercase()
                ?.contains(string.lowercase()) ?: true || place.category?.lowercase()
                ?.contains(string.lowercase()) ?: true)
        }
    }

    private val _navigateToPlaceCreate = MutableLiveData<Boolean?>()
    val navigateToPlaceCreate: LiveData<Boolean?>
        get() = _navigateToPlaceCreate

    private val _navigateToPlaceDetails = MutableLiveData<String?>()
    val navigateToPlaceDetails: LiveData<String?>
        get() = _navigateToPlaceDetails

    fun onPlaceDetailsClicked(placeId: String) {
        _navigateToPlaceDetails.value = placeId
    }

    fun onPlaceCreateClicked() {
        _navigateToPlaceCreate.value = true
    }

    fun onPlaceCreateNavigated() {
        _navigateToPlaceCreate.value = null
    }

    fun onPlaceDetailsNavigated() {
        _navigateToPlaceDetails.value = null
    }

    private suspend fun insert(history: History) {
        if (databaseHistory.get(history.search) == null) {
            databaseHistory.insert(history)
        }
    }

    fun addToHistory() {
        viewModelScope.launch {
            search.value?.let { History(it) }?.let { insert(it) }
        }
    }
}
