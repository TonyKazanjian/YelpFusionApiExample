package com.tonykazanjian.sonyyelpfusion.ui

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
    private lateinit var businessDetailViewModel: BusinessDetailViewModel

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
        Linkify.addLinks(binding.businessDetailUrlTextView, Linkify.WEB_URLS
        )

        //TODO - init viewpager
//        val imageAdapter = initViewPager()
//        initViewModel(binding, imageAdapter)
//        initSubBreedRecyclerView()

        //TODO - init recyclerview

        businessDetailViewModel = ViewModelProvider(this, viewModelFactory).get(BusinessDetailViewModel::class.java)

        businessDetailViewModel.fetchBusinessDetail(businessAlias)

        businessDetailViewModel.reviewsLiveData.observe(viewLifecycleOwner, Observer { reviews ->
            Log.d("TONY", "$reviews")
            binding.viewModel = businessDetailViewModel

        })

        businessDetailViewModel.isLoading().observe(viewLifecycleOwner, Observer { isLoading ->
            if (!isLoading){
                binding.businessDetailRatingBar.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }
        })


        return binding.root
    }

//    private fun initViewPager(): BreedImageAdapter {
//        val viewPager = binding.viewPager
//        val imageAdapter = BreedImageAdapter(context)
//        viewPager.adapter = imageAdapter
//        return imageAdapter
//    }

    companion object {
        const val ARG_BUSINESS_ALIAS = "business_alias"
        const val ARG_BUSINESS_NAME = "business_name"
    }
}
