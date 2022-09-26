package mobg5.g55047.projet.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Rating {
    @DocumentId
    var ratingId: String? = null
    var userId: String? = null
    var userName: String? = null
    var placeId: String? = null

    @ServerTimestamp
    var timestamp: Date? = null
    var description: String? = null
    var rating: Double = 0.0

    constructor()

    constructor(
        userId: String?,
        userName: String?,
        placeId: String?,
        description: String?,
        rating: Double
    ) {
        this.userId = userId
        this.userName = userName
        this.placeId = placeId
        this.description = description
        this.rating = rating
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rating

        if (ratingId != other.ratingId) return false
        if (userId != other.userId) return false
        if (userName != other.userName) return false
        if (placeId != other.placeId) return false
        if (timestamp != other.timestamp) return false
        if (description != other.description) return false
        if (rating != other.rating) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ratingId?.hashCode() ?: 0
        result = 31 * result + (userId?.hashCode() ?: 0)
        result = 31 * result + (userName?.hashCode() ?: 0)
        result = 31 * result + (placeId?.hashCode() ?: 0)
        result = 31 * result + (timestamp?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + rating.hashCode()
        return result
    }
}
