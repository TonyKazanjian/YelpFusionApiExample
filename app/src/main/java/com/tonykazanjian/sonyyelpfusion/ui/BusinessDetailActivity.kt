package com.tonykazanjian.sonyyelpfusion.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.tonykazanjian.sonyyelpfusion.R
import kotlinx.android.synthetic.main.activity_business_detail.*

/**
 * An activity representing a single Business detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [BusinessListActivity].
 */
class BusinessDetailActivity : BaseActivity(), BusinessDetailFragment.ImageListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_detail)
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            val fragment = BusinessDetailFragment().apply {
                setImageListener(this@BusinessDetailActivity)
                arguments = Bundle().apply {
                    putString(
                        BusinessDetailFragment.ARG_BUSINESS_ALIAS,
                        intent.getStringExtra(BusinessDetailFragment.ARG_BUSINESS_ALIAS)
                    )
                    putString(
                        BusinessDetailFragment.ARG_BUSINESS_NAME,
                        intent.getStringExtra(BusinessDetailFragment.ARG_BUSINESS_NAME)
                    )
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.business_detail_container, fragment)
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, BusinessListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onImagesReceived(imageList: List<String>) {
        val viewPager = pager
        val imageAdapter = BusinessImageAdapter()
        viewPager.adapter = imageAdapter
        imageAdapter.setImages(imageList)
    }
}
