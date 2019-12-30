package com.tonykazanjian.sonyyelpfusion.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Annotation
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tonykazanjian.sonyyelpfusion.R
import com.tonykazanjian.sonyyelpfusion.data.Business
import com.tonykazanjian.sonyyelpfusion.databinding.BusinessDetailBinding
import com.tonykazanjian.sonyyelpfusion.di.DaggerAppComponent
import com.tonykazanjian.sonyyelpfusion.di.ViewModelFactory
import com.tonykazanjian.sonyyelpfusion.ui.viewmodels.BusinessDetailViewModel
import kotlinx.android.synthetic.main.activity_business_detail.*
import javax.inject.Inject

/**
 * A fragment representing a single Business detail screen.
 * This fragment is either contained in a [BusinessListActivity]
 * in two-pane mode (on tablets) or a [BusinessDetailActivity]
 * on handsets.
 */
open class BusinessDetailFragment : Fragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    private var businessAlias: String? = null

    private lateinit var binding: BusinessDetailBinding

    private var imageListener: ImageListener? = null

    private val reviewAdapter = ReviewListAdapter({ reviewUrl ->
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(reviewUrl)
            startActivity(this)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAppComponent.create().inject(this)

        arguments?.let {
            activity?.toolbar_layout?.title = it.getString(ARG_BUSINESS_NAME)
            businessAlias = it.getString(ARG_BUSINESS_ALIAS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.business_detail, container, false)

        binding.reviewsRecyclerView.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        ViewModelProvider(this, viewModelFactory).get(BusinessDetailViewModel::class.java).apply {

            fetchBusinessDetail(businessAlias)

            getBusinessLiveData().observe(viewLifecycleOwner, Observer {business ->
                binding.viewModel = this
                if (business.price.isNullOrEmpty()){
                    binding.businessDetailPriceTextView.visibility = View.GONE
                }
                if (business.phoneNumber.isNullOrEmpty()){
                    binding.businessDetailPhoneTextView.visibility = View.GONE
                }

                binding.businessDetailUrlTextView.setSpannableLink(business)
            })

            getPhotosLiveData().observe(viewLifecycleOwner, Observer { photos ->
                imageListener?.onImagesReceived(photos)
            })

            getReviewsLiveData().observe(viewLifecycleOwner, Observer { reviews ->
                reviewAdapter.setData(reviews)
            })

            isLoading().observe(viewLifecycleOwner, Observer { isLoading ->
                if (!isLoading){
                    binding.businessDetailRatingBar.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                } else {
                    binding.progressBar.visibility = View.VISIBLE
                }
            })

            isError().observe(viewLifecycleOwner, Observer { isError ->
                if (isError){
                    binding.errorTextView.visibility = View.VISIBLE
                } else {
                    binding.errorTextView.visibility = View.INVISIBLE
                }
            })
        }

        return binding.root
    }

    fun setImageListener(imageListener: ImageListener){
        this.imageListener = imageListener
    }

    private fun TextView.setSpannableLink(business: Business) {
        val linkText = SpannedString(getText(R.string.business_url_prompt))
        val spannableString = SpannableString(linkText)

        val annotations = linkText.getSpans(0, linkText.length, Annotation::class.java)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(business.businessUrl)
                    startActivity(this)
                }
            }
        }

        annotations?.find { it.value == "yelp_link" }?.let {
            spannableString.apply {
                setSpan(
                    clickableSpan, linkText.getSpanStart(it), linkText.getSpanEnd(it),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)),
                    linkText.getSpanStart(it), linkText.getSpanEnd(it), 0
                )
            }
        }

        this.text = spannableString
        movementMethod = LinkMovementMethod.getInstance()
    }

    companion object {
        const val ARG_BUSINESS_ALIAS = "business_alias"
        const val ARG_BUSINESS_NAME = "business_name"
        const val ARG_BUSINESS_IMAGE_URL = "business_image"
    }

    interface ImageListener {
        fun onImagesReceived(imageList: List<String>)
    }
}
