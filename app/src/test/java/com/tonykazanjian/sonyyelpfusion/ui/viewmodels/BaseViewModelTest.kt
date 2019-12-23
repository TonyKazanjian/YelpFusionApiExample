package com.tonykazanjian.sonyyelpfusion.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import com.tonykazanjian.sonyyelpfusion.RxImmediateSchedulerRule
import io.reactivex.schedulers.TestScheduler
import org.junit.Rule
import org.mockito.Mockito.mock

open class BaseViewModelTest {

    inline fun <reified T> mock(): T = mock(T::class.java)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

}