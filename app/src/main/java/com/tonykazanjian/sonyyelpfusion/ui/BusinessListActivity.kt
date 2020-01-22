package com.tonykazanjian.sonyyelpfusion.ui

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tonykazanjian.sonyyelpfusion.R
import com.tonykazanjian.sonyyelpfusion.data.Business
import com.tonykazanjian.sonyyelpfusion.databinding.ActivityBusinessListBinding
import com.tonykazanjian.sonyyelpfusion.ui.viewmodels.BusinessListViewModel
import kotlinx.android.synthetic.main.activity_business_list.*
import pub.devrel.easypermissions.EasyPermissions

fun Context.startDetailActivity(activity: Activity, imageView: ImageView, business: Business){
    val intent = Intent(this, BusinessDetailActivity::class.java).apply {
        putExtra(BusinessDetailFragment.ARG_BUSINESS_ALIAS, business.alias)
        putExtra(BusinessDetailFragment.ARG_BUSINESS_NAME, business.name)
        putExtra(BusinessDetailFragment.ARG_BUSINESS_IMAGE_URL, business.imageUrl)
    }
    val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
        activity, Pair.create(imageView, getString(R.string.transition_image))
    )
    startActivity(intent, activityOptions.toBundle())
}
/**
 * An activity representing a list of Businsesses matching a search term. This activity
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

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var businessListViewModel: BusinessListViewModel
    private lateinit var binding: ActivityBusinessListBinding

    private var businessAdapter = BusinessListAdapter( { business, imageView ->
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
            startDetailActivity(this, imageView, business)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_business_list)
        businessListViewModel = ViewModelProvider(this, viewModeFactory).get(BusinessListViewModel::class.java)

        requestPermissions()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setSearchLocation()

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
            binding.searchPromptTextView?.visibility = View.INVISIBLE
            if (it.isEmpty()){
                binding.emptyListTextView?.visibility = View.VISIBLE
            } else {
                binding.emptyListTextView?.visibility = View.INVISIBLE
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

        businessListViewModel.isError().observe(this, Observer { isError ->
            binding.searchPromptTextView?.visibility = View.INVISIBLE
            if (!isError){
                binding.progressBar.visibility = View.INVISIBLE
                binding.errorTextView?.visibility = View.INVISIBLE
            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.errorTextView?.visibility = View.VISIBLE

            }
        })
    }

    private fun setSearchLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            businessListViewModel.latitude = location?.latitude.toString()
            businessListViewModel.longitude = location?.longitude.toString()

        }
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

    override fun onDestroy() {
        super.onDestroy()
        businessListViewModel.clearDisposable()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        setSearchLocation()
    }

    private fun requestPermissions() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, "This app requires location permissions to search for local businesses",
                0, Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
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
