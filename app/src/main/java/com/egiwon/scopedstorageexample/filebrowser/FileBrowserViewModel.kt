package com.egiwon.scopedstorageexample.filebrowser

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egiwon.scopedstorageexample.Event
import com.egiwon.scopedstorageexample.base.BaseViewModel
import com.egiwon.scopedstorageexample.model.DocumentItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class FileBrowserViewModel : BaseViewModel() {

    private val _directoryUri = MutableLiveData<Event<Uri>>()
    val directoryUri: LiveData<Event<Uri>> get() = _directoryUri

    private val _documents = MutableLiveData<List<DocumentItem>>()
    val documents: LiveData<List<DocumentItem>> get() = _documents

    private val _loadingBar = MutableLiveData<Boolean>()
    val loadingBar: LiveData<Boolean> get() = _loadingBar

    private val _documentList = mutableListOf<DocumentItem>()

    fun setDirectoryUri(directoryUri: Uri) {
        _directoryUri.value = Event(directoryUri)
    }

    fun loadDirectory(documentFile: DocumentFile) {
        Observable.create<DocumentItem> { emit ->
            _documentList.clear()
            documentFile.listFiles().forEach { emit.onNext(DocumentItem(it)) }
            emit.onComplete()
        }
            .doOnSubscribe { _loadingBar.postValue(true) }
            .doOnComplete { _loadingBar.postValue(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    _documentList.add(it)
                    _documents.value = _documentList
                },

                onComplete = {
                    _documents.value = _documentList
                        .asSequence()
                        .filter { !(it.name?.startsWith(".") ?: true) }
                        .sortedWith(compareBy({ !it.isDirectory }, { it.name?.toLowerCase() }))
                        .toList()
                }
            )
            .addTo(compositeDisposable)
    }
}