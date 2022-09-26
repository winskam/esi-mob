package mobg5.g55047.projet.screens.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import mobg5.g55047.projet.R
import mobg5.g55047.projet.database.FirebasePlaces
import mobg5.g55047.projet.databinding.FragmentRatingBinding

/**
 * Dialog Fragment containing rating form.
 */
class RatingDialogFragment(val placeId: String) : DialogFragment(), View.OnClickListener {
    private lateinit var ratingViewModel: RatingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentRatingBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_rating, container, false
        )
        val application = requireNotNull(this.activity).application
        // Create an instance of the ViewModel Factory.
        val dataSource = FirebasePlaces
        val viewModelFactory = RatingViewModelFactory(dataSource, placeId)
        // Get a reference to the ViewModel associated with this fragment.
        val ratingViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(RatingViewModel::class.java)

        this.ratingViewModel = ratingViewModel

        binding.submitReviewBtn.setOnClickListener {
            ratingViewModel.onSubmit()
        }

        ratingViewModel.validReview.observe(
            viewLifecycleOwner,
            { status ->
                status?.let {
                    if (ratingViewModel.validReview.value == true) {
                        showToast("Review added !")
                        onSubmitClicked()
                    } else {
                        showToast("Invalid field(s)")
                    }
                    ratingViewModel.validReview.value = null
                }
            })

        binding.cancelReviewBtn.setOnClickListener {
            onCancelClicked()
        }

        binding.placeRatingReview.onRatingChangeListener =
            MaterialRatingBar.OnRatingChangeListener { _, rating ->
                ratingViewModel.rating.value = rating.toDouble()
            }

        binding.ratingViewModel = ratingViewModel

        return binding.root
    }

    private fun onSubmitClicked() {
        dismiss()
    }

    private fun onCancelClicked() {
        dismiss()
    }

    companion object {
        const val TAG = "RatingDialog"
    }

    override fun onClick(v: View?) {
    }

    private fun showToast(textToast: String) {
        Toast.makeText(context, textToast, Toast.LENGTH_SHORT).show()
    }
}
