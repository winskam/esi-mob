package mobg5.g55047.projet.screens

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import mobg5.g55047.projet.R
import mobg5.g55047.projet.model.Place
import mobg5.g55047.projet.model.Rating
import java.text.SimpleDateFormat

@BindingAdapter("placeNameString")
fun TextView.setName(item: Place?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("placeCategoryString")
fun TextView.setCategory(item: Place?) {
    item?.let {
        text = item.category
    }
}

@BindingAdapter("placeRating")
fun MaterialRatingBar.setRating(item: Place?) {
    item?.let {
        rating = item.avgRating.toFloat()
    }
}

@BindingAdapter("ratingCount")
fun TextView.setRating(item: Place?) {
    item?.let {
        text = item.numRatings.toString()
    }
}

@BindingAdapter("placeCityString")
fun TextView.setCity(item: Place?) {
    item?.let {
        text = item.city
    }
}

@BindingAdapter("nameRating")
fun TextView.setNameRating(item: Rating?) {
    item?.let {
        text = item.userName
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("dateRating")
fun TextView.setDateRating(item: Rating?) {
    item?.let {
        val date = it.timestamp
        if (date != null) {
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val formatted = formatter.format(date)
            text = formatted
        }
    }
}

@BindingAdapter("avgRating")
fun MaterialRatingBar.setAvgRating(item: Rating?) {
    item?.let {
        rating = item.rating.toFloat()
    }
}

@BindingAdapter("descriptionRating")
fun TextView.setDescriptionRating(item: Rating?) {
    item?.let {
        text = item.description
    }
}

@BindingAdapter("placeImage")
fun ImageView.setImage(item: Place?) {
    item?.let {
        setImageResource(
            when (item.category) {
                "Restaurant" -> R.drawable.restaurant
                "Museum" -> R.drawable.museum
                "Monument" -> R.drawable.monument
                "Park" -> R.drawable.park
                "School" -> R.drawable.school
                else -> R.drawable.other
            }
        )
    }
}
