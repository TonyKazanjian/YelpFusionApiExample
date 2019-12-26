package com.tonykazanjian.sonyyelpfusion.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tonykazanjian.sonyyelpfusion.R
import com.tonykazanjian.sonyyelpfusion.di.DaggerAppComponent
import kotlinx.android.synthetic.main.activity_business_detail.*
import kotlinx.android.synthetic.main.business_detail.view.*

/**
 * A fragment representing a single Business detail screen.
 * This fragment is either contained in a [BusinessListActivity]
 * in two-pane mode (on tablets) or a [BusinessDetailActivity]
 * on handsets.
 */
class BusinessDetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAppComponent.create().inject(this)

//        arguments?.let {
//            if (it.containsKey(ARG_ITEM_ID)) {
//                // Load the dummy content specified by the fragment
//                // arguments. In a real-world scenario, use a Loader
//                // to load content from a content provider.
////                item = DummyContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
//                activity?.toolbar_layout?.title = item?.content
//            }
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.business_detail, container, false)

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
