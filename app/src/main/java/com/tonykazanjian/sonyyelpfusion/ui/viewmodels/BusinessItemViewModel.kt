package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.tonykazanjian.sonyyelpfusion.data.Business

class BusinessItemViewModel (business: Business): ViewModel() {
    val title = business.name
}