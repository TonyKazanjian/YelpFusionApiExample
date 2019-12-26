package com.tonykazanjian.sonyyelpfusion.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tonykazanjian.sonyyelpfusion.R
import com.tonykazanjian.sonyyelpfusion.databinding.ActivityBusinessListBinding

import com.tonykazanjian.sonyyelpfusion.ui.viewmodels.BusinessListViewModel
import kotlinx.android.synthetic.main.activity_business_list.*
import kotlinx.android.synthetic.main.business_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [BusinessDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class BusinessListActivity : BaseActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    lateinit var businessListViewModel: BusinessListViewModel
    lateinit var binding: ActivityBusinessListBinding

    private val businessAdapter = BusinessListAdapter( {
        business ->
        if (twoPane) {
            val fragment = BusinessDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(BusinessDetailFragment.ARG_ITEM_ID, business.alias)
                }
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.business_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(this, BusinessDetailActivity::class.java).apply {
                putExtra(BusinessDetailFragment.ARG_ITEM_ID, business.alias)
            }
            startActivity(intent)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_business_list)
        businessListViewModel = ViewModelProvider(this, viewModeFactory).get(BusinessListViewModel::class.java)

        binding.apply {
            setSupportActionBar(toolbar)
            toolbar.title = title
            businessListRecyclerView.apply {
                adapter = businessAdapter
                layoutManager = LinearLayoutManager(this.context)
                addItemDecoration(
                    DividerItemDecoration(
                        this.context,
                        LinearLayout.VERTICAL
                    )
                )
            }
            lifecycleOwner = this@BusinessListActivity
            viewModel = businessListViewModel
        }

        if (business_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        businessListViewModel.getBusinesses().observe(this, Observer {
            if (it.isEmpty()){
                //TODO - show no items message
            } else {
                businessAdapter.setQueryData(it)
            }
        })

        businessListViewModel.isLoading().observe(this, Observer { isLoading ->
            if (!isLoading){
                binding.progressBar.visibility = View.INVISIBLE
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent?.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            businessListViewModel.fetchBusinesses(query)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return true
    }
}
