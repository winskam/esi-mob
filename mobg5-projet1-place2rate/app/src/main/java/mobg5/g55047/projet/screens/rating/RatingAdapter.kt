package mobg5.g55047.projet.screens.rating

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mobg5.g55047.projet.databinding.ListItemRatingBinding
import mobg5.g55047.projet.model.Rating

class RatingAdapter : ListAdapter<Rating, RatingAdapter.ViewHolder>(RatingDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: ListItemRatingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Rating) {
            binding.rating = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRatingBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class RatingDiffCallback : DiffUtil.ItemCallback<Rating>() {
    override fun areItemsTheSame(oldItem: Rating, newItem: Rating): Boolean {
        return oldItem.ratingId == newItem.ratingId
    }

    override fun areContentsTheSame(oldItem: Rating, newItem: Rating): Boolean {
        return oldItem == newItem
    }
}
