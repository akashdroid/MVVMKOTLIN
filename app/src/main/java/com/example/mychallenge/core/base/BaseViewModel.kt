package com.example.mychallenge.core.base


import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel

import com.example.mychallenge.core.rx.SchedulerProvider

import java.lang.ref.WeakReference

import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel<N>(
    val dataManager: DataManager,
    val schedulerProvider: SchedulerProvider
) : ViewModel() {

    val isLoading = ObservableBoolean(false)

    val compositeDisposable: CompositeDisposable

    private var mNavigator: WeakReference<N>? = null

    var navigator: N
        get() = mNavigator!!.get()!!
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }

    init {
        this.compositeDisposable = CompositeDisposable()
    }

    protected override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }
}
