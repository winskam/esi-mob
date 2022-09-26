package mobg5.g55047.projet.screens.place

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mobg5.g55047.projet.databinding.ListItemPlaceBinding
import mobg5.g55047.projet.model.Place
import java.util.*
import kotlin.collections.ArrayList

class PlaceAdapter(val clickListener: PlaceListener) :
    ListAdapter<Place, PlaceAdapter.ViewHolder>(PlaceDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: ListItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Place, clickListener: PlaceListener) {
            binding.place = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPlaceBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.placeId == newItem.placeId
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}


class PlaceListener(val clickListener: (placeId: String) -> Unit) {
    fun onClick(place: Place) = place.placeId?.let { clickListener(it) }
}
