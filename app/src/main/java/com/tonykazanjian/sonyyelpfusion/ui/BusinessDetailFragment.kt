package com.tonykazanjian.sonyyelpfusion.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tonykazanjian.sonyyelpfusion.R
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
class BusinessDetailFragment : Fragment() {

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
        binding.businessDetailUrlTextView.text = "Visit site on Yelp"
        //TODO - fix how text is displayed
        Linkify.addLinks(binding.businessDetailUrlTextView, Linkify.WEB_URLS)

        binding.reviewsRecyclerView.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        ViewModelProvider(this, viewModelFactory).get(BusinessDetailViewModel::class.java).apply {
            fetchBusinessDetail(businessAlias)

            businessLiveData.observe(viewLifecycleOwner, Observer {
                binding.viewModel = this
                if (it.price.isNullOrEmpty()){
                    binding.businessDetailPriceTextView.visibility = View.GONE
                }
                if (it.phoneNumber.isNullOrEmpty()){
                    binding.businessDetailPhoneTextView.visibility = View.GONE
                }
            })

            photosLiveData.observe(viewLifecycleOwner, Observer { photos ->
                imageListener?.onImagesReceived(photos)
            })

            reviewsLiveData.observe(viewLifecycleOwner, Observer { reviews ->
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
        }

        return binding.root
    }

    fun setImageListener(imageListener: ImageListener){
        this.imageListener = imageListener
    }

    companion object {
        const val ARG_BUSINESS_ALIAS = "business_alias"
        const val ARG_BUSINESS_NAME = "business_name"
    }

    interface ImageListener {
        fun onImagesReceived(imageList: List<String>)
    }
}
