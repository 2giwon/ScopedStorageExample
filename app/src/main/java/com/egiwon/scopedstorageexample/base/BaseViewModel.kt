package com.egiwon.scopedstorageexample.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    protected val mutableErrorTextResId = MutableLiveData<Int>()
    val errorTextResId: LiveData<Int> get() = mutableErrorTextResId

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    open fun onClick(model: Any?) {}
}