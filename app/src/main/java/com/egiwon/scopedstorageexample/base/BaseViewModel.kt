package com.egiwon.scopedstorageexample.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    protected val errorThrowableMutableLiveData = MutableLiveData<Throwable>()
    val errorThrowableLiveData: LiveData<Throwable> get() = errorThrowableMutableLiveData

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    open fun onClick(model: Any?) {}
}