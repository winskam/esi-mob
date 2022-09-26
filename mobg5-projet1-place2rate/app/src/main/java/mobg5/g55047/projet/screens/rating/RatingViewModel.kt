package mobg5.g55047.projet.screens.rating

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mobg5.g55047.projet.database.FirebasePlaces

class RatingViewModel(
    dataSource: FirebasePlaces.Companion,
    val placeId: String
) : ViewModel() {
    val description: MutableLiveData<String?> = MutableLiveData()
    val rating: MutableLiveData<Double> = MutableLiveData()
    val database = dataSource
    private val _validReview = MutableLiveData<Boolean?>()
    val validReview: MutableLiveData<Boolean?>
        get() = _validReview

    fun onSubmit() {
        if (description.value != null && rating.value != null) {
            database.addRating(placeId, description.value!!, rating.value!!)
            validReview.value = true
        } else {
            validReview.value = false
        }
    }
}
