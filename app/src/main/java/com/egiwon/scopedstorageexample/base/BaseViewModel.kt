package com.egiwon.scopedstorageexample.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val compositeDisposable = CompositeDisposable()

    protected val errorThrowableMutableLiveData = MutableLiveData<Throwable>()
    val errorThrowableLiveData: LiveData<Throwable> get() = errorThrowableMutableLiveData

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    open fun onClick(model: Any?) {}
}