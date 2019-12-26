package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.tonykazanjian.sonyyelpfusion.data.Business

class BusinessItemViewModel (val business: Business): ViewModel() {
    val title = business.name
    val price = business.price
    val rating = business.rating

    fun getStatus(): String {
        return if (business.isClosed) "Closed" else "Open"
    }
}