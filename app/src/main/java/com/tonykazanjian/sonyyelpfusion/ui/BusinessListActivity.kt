package com.tonykazanjian.sonyyelpfusion.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tonykazanjian.sonyyelpfusion.R
import com.tonykazanjian.sonyyelpfusion.databinding.ActivityBusinessListBinding

import com.tonykazanjian.sonyyelpfusion.ui.viewmodels.BusinessListViewModel
import kotlinx.android.synthetic.main.activity_business_list.*

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

    private lateinit var businessListViewModel: BusinessListViewModel
    private lateinit var binding: ActivityBusinessListBinding

    private var businessAdapter = BusinessListAdapter( { business ->
        if (twoPane) {
            val fragment = BusinessDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(BusinessDetailFragment.ARG_BUSINESS_ALIAS, business.alias)
                    putString(BusinessDetailFragment.ARG_BUSINESS_NAME, business.name)
                }
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.business_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(this, BusinessDetailActivity::class.java).apply {
                putExtra(BusinessDetailFragment.ARG_BUSINESS_ALIAS, business.alias)
                putExtra(BusinessDetailFragment.ARG_BUSINESS_NAME, business.name)
            }
            startActivity(intent)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_business_list)
        businessListViewModel = ViewModelProvider(this, viewModeFactory).get(BusinessListViewModel::class.java)

        if (business_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        binding.apply {
            setSupportActionBar(toolbar)
            toolbar.title = title
            lifecycleOwner = this@BusinessListActivity
            viewModel = businessListViewModel
        }

        binding.businessListRecyclerView.apply {
            adapter = businessAdapter
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    LinearLayout.VERTICAL
                )
            )
            addOnScrollListener(object : PaginationScrollListener(layoutManager as LinearLayoutManager){
                override fun loadMoreItems() {
                    businessListViewModel.fetchBusinesses("", 20)
                }

                override fun isLoading(): Boolean {
                    businessListViewModel.isLoading().value?.let { loading ->
                        return loading
                    }
                    return false
                }
            })
        }

        businessListViewModel.getBusinesses().observe(this, Observer {
            if (it.isEmpty()){
                //TODO - show no items message
            } else {
                businessAdapter.addData(it)
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
            businessAdapter.clearItems()
            intent.getStringExtra(SearchManager.QUERY).apply {
                businessAdapter.clearItems()
                businessListViewModel.searchTerm = this
                businessListViewModel.fetchBusinesses(this)

            }
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

    abstract class PaginationScrollListener(private var layoutManager: LinearLayoutManager) :
        RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!isLoading()) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreItems()
                }
            }
        }

        protected abstract fun loadMoreItems()
        protected abstract fun isLoading(): Boolean
    }
}
