package mobg5.g55047.projet.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import mobg5.g55047.projet.model.Place
import mobg5.g55047.projet.model.Rating
import java.util.*

abstract class FirebasePlaces {
    val firebasePlaces = FirebasePlaces

    companion object {

        fun getInstance(): Companion {
            return FirebasePlaces
        }

        fun getPlaces(): LiveData<List<Place>> {
            val allPlaces: MutableLiveData<List<Place>> = MutableLiveData()
            FirebaseFirestore.getInstance().collection("places")
                .addSnapshotListener(EventListener { value, e ->
                    if (e != null) {
                        return@EventListener
                    }
                    val places: MutableList<Place> = mutableListOf()
                    for (doc in value!!) {
                        val place = doc.toObject(Place::class.java)
                        places.add(place)
                    }
                    allPlaces.value = places
                })
            return allPlaces
        }

        fun addPlace(
            name: String,
            longitude: Double,
            latitude: Double,
            city: String,
            category: String?
        ) {
            val place = Place(name, longitude, latitude, city, category, 0, 0.0)
            FirebaseFirestore.getInstance().collection("places").add(place)
        }

        fun getPlace(placeId: String): MutableLiveData<Place> {
            val place: MutableLiveData<Place> = MutableLiveData()
            FirebaseFirestore.getInstance().collection("places").document(placeId)
                .addSnapshotListener(EventListener { value, e ->
                    if (e != null) {
                        return@EventListener
                    }
                    val placeResult = value?.toObject(Place::class.java)
                    if (placeResult != null) {
                        place.value = placeResult
                    }
                })
            return place
        }

        fun addRating(placeId: String, description: String, rating: Double) {
            var newRating: Double
            val document =
                FirebaseFirestore.getInstance().collection("places").document(placeId).get()
            document.addOnCompleteListener {
                val ob = it.result.toObject(Place::class.java)
                val currentRating = ob!!.avgRating
                val currentCount = ob.numRatings
                newRating = (currentRating * currentCount + rating) / (currentCount + 1)
                val userRef = FirebaseFirestore.getInstance().collection("places").document(placeId)
                val increment = FieldValue.increment(1)
                userRef.update("numRatings", increment)
                userRef.update("avgRating", newRating)
            }
            FirebaseFirestore.getInstance().collection("ratings").add(
                Rating(
                    FirebaseUtil.getAuth()!!.uid,
                    FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                    placeId,
                    description,
                    rating
                )
            )
        }

        fun getRatings(placeId: String): MutableLiveData<List<Rating>> {
            val allRatings: MutableLiveData<List<Rating>> = MutableLiveData()
            FirebaseFirestore.getInstance().collection("ratings").whereEqualTo("placeId", placeId)
                .addSnapshotListener(EventListener { value, e ->
                    if (e != null) {
                        return@EventListener
                    }
                    val ratings: MutableList<Rating> = mutableListOf()
                    for (doc in value!!) {
                        val rating = doc.toObject(Rating::class.java)
                        ratings.add(rating)
                    }
                    ratings.sortByDescending { rating ->
                        rating.timestamp
                    }
                    allRatings.value = ratings
                })
            return allRatings
        }
    }
}
