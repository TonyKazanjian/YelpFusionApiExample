package com.tonykazanjian.sonyyelpfusion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.tonykazanjian.sonyyelpfusion.R
import kotlinx.android.synthetic.main.business_image.view.*

class BusinessImageAdapter: PagerAdapter() {
    var imageList = listOf<String>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.business_image, container, false)
        container.addView(view)
        Glide.with(view.context)
            .load(imageList[position])
            .into(view.image)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return imageList.size
    }

    fun setImages(images: List<String>){
        imageList = images
        notifyDataSetChanged()
    }
}