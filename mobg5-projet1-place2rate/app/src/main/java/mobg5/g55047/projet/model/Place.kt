package mobg5.g55047.projet.model

import com.google.firebase.firestore.DocumentId

class Place {
    @DocumentId
    var placeId: String? = null
    var name: String? = null
    var longitude: Double = 0.0
    var latitude: Double = 0.0
    var city: String? = null
    var category: String? = null
    var numRatings = 0
    var avgRating = 0.0

    constructor()

    constructor(
        name: String?,
        longitude: Double,
        latitude: Double,
        city: String,
        category: String?,
        numRatings: Int,
        avgRating: Double
    ) {
        this.name = name
        this.longitude = longitude
        this.latitude = latitude
        this.city = city
        this.category = category
        this.numRatings = numRatings
        this.avgRating = avgRating
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Place

        if (placeId != other.placeId) return false
        if (name != other.name) return false
        if (longitude != other.longitude) return false
        if (latitude != other.latitude) return false
        if (city != other.city) return false
        if (category != other.category) return false
        if (numRatings != other.numRatings) return false
        if (avgRating != other.avgRating) return false

        return true
    }

    override fun hashCode(): Int {
        var result = placeId?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + longitude.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + numRatings
        result = 31 * result + avgRating.hashCode()

        return result
    }
}
