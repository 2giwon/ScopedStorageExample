package com.egiwon.scopedstorageexample.image

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egiwon.scopedstorageexample.base.BaseViewModel
import com.egiwon.scopedstorageexample.model.MediaStoreImage
import com.egiwon.scopedstorageexample.util.image.ImageProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ImagePickerViewModel @ViewModelInject constructor(
    private val imageProvider: ImageProvider
) : BaseViewModel() {

    private val _images = MutableLiveData<List<MediaStoreImage>>()
    val images: LiveData<List<MediaStoreImage>> get() = _images

    private val _loadingBar = MutableLiveData<Boolean>()
    val loadingBar: LiveData<Boolean> get() = _loadingBar

    fun loadImages() {
        queryImages()
            .doOnSubscribe { _loadingBar.postValue(true) }
            .doAfterTerminate { _loadingBar.postValue(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                _images.value = it
            }.addTo(compositeDisposable)
    }

    private fun queryImages(): Single<List<MediaStoreImage>> {
        return Single.create { emitter ->
            emitter.onSuccess(imageProvider.getImages())
        }
    }

}