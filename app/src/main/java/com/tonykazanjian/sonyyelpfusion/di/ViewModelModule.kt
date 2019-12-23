package com.tonykazanjian.sonyyelpfusion.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tonykazanjian.sonyyelpfusion.ui.viewmodels.BusinessDetailViewModel
import com.tonykazanjian.sonyyelpfusion.ui.viewmodels.BusinessItemViewModel
import com.tonykazanjian.sonyyelpfusion.ui.viewmodels.BusinessListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author Tony Kazanjian
 */
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BusinessListViewModel::class)
    abstract fun bindListViewModel(listViewModel: BusinessListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BusinessDetailViewModel::class)
    abstract fun bindDetailViewModel(detailViewModel: BusinessDetailViewModel): ViewModel
}