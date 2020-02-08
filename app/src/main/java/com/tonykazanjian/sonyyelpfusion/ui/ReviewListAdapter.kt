package com.tonykazanjian.sonyyelpfusion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tonykazanjian.sonyyelpfusion.R
import com.tonykazanjian.sonyyelpfusion.databinding.ReviewItemBinding
import com.tonykazanjian.sonyyelpfusion.domain.Review
import com.tonykazanjian.sonyyelpfusion.ui.viewmodels.ReviewItemViewModel

class ReviewListAdapter(private var onItemClickListener: (String) -> Unit, var reviews: List<Review> = listOf())
    : RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder>(){

    private lateinit var binding: ReviewItemBinding

    fun setData(list: List<Review>){
        this.reviews = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.review_item, parent, false)
        return ReviewViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
        holder.itemView.setOnClickListener{onItemClickListener(review.reviewUrl)}
    }

    inner class ReviewViewHolder(val view: View): RecyclerView.ViewHolder(view){
        fun bind(review: Review){
            val viewModel = ReviewItemViewModel(review)
            binding.reviewDateTextView.text = binding.root.context.getString(R.string.review_date, viewModel.reviewDate)
            binding.viewModel = viewModel
            Glide.with(view.context)
                .load(review.user.imageUrl)
                .circleCrop()
                .into(binding.reviewerAvatarImageView)
        }
    }
}