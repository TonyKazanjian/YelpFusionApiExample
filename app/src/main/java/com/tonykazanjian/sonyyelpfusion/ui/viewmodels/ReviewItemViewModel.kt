package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.tonykazanjian.sonyyelpfusion.data.Review
import java.text.SimpleDateFormat
import java.util.*

class ReviewItemViewModel(val review: Review): ViewModel() {

    val reviewText = review.reviewText
    val reviewDate = review.reviewDate.toDate()?.formatTo("MMM dd yyyy")
    val reviewUrl = review.user
    val reviewRating = review.rating
    val reviewUserName = review.user.name


    fun String.toDate(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date? {
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this)
    }

    fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }
}