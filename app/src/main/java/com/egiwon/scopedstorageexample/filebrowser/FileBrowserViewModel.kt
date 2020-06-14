package com.egiwon.scopedstorageexample.filebrowser

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egiwon.scopedstorageexample.Event
import com.egiwon.scopedstorageexample.base.BaseViewModel
import com.egiwon.scopedstorageexample.data.FileBrowserRepository
import com.egiwon.scopedstorageexample.model.DocumentItem
import com.egiwon.scopedstorageexample.util.DocumentProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

class FileBrowserViewModel(
    private val repository: FileBrowserRepository,
    private val documentProvider: DocumentProvider
) : BaseViewModel() {

    private val _directoryUri = MutableLiveData<Event<Uri>>()
    val directoryUri: LiveData<Event<Uri>> get() = _directoryUri

    private val _documents = MutableLiveData<List<DocumentItem>>()
    val documents: LiveData<List<DocumentItem>> get() = _documents

    private val _loadingBar = MutableLiveData<Boolean>()
    val loadingBar: LiveData<Boolean> get() = _loadingBar

    private val _directoryLiveData = MutableLiveData<Event<DocumentItem>>()
    val directoryLiveData: LiveData<Event<DocumentItem>> get() = _directoryLiveData

    private val _documentLiveData = MutableLiveData<Event<DocumentItem>>()
    val documentLiveData: LiveData<Event<DocumentItem>> get() = _documentLiveData

    private val documentList = mutableListOf<DocumentItem>()

    private val breadCrumbsStack = Stack<Uri>()

    private val _isBackBreadCrumbsEmpty = MutableLiveData<Event<Boolean>>()
    val isBackBreadCrumbsEmpty: LiveData<Event<Boolean>> get() = _isBackBreadCrumbsEmpty

    override fun onClick(model: Any?) {
        (model as? DocumentItem)?.let { document ->
            if (document.isDirectory) {
                _directoryLiveData.postValue(Event(document))
                addBreadCrumbs(_directoryUri.value?.peekContent() ?: return)
            } else {
                _documentLiveData.postValue(Event(document))
            }
        }
    }

    fun setDirectoryUri(directoryUri: Uri) {
        _directoryUri.value = Event(directoryUri)
    }

    fun loadDirectory(documentUri: Uri) {
        Observable.create<List<DocumentItem>> { emit ->
            documentList.clear()
            runCatching {
                val resultList = documentProvider.getDocuments(documentUri)
                documentList.addAll(resultList)
            }
                .onSuccess { emit.onComplete() }
                .onFailure { emit.onError(it) }
        }
            .doOnSubscribe { _loadingBar.postValue(true) }
            .doAfterTerminate { _loadingBar.postValue(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    errorThrowableMutableLiveData.value = it
                },
                onComplete = {
                    _documents.value = documentList
                        .asSequence()
                        .filter { !(it.name.startsWith(".")) }
                        .sortedWith(compareBy({ !it.isDirectory }, {
                            it.name.toLowerCase(Locale.getDefault())
                        }))
                        .toList()
                }
            )
            .addTo(compositeDisposable)
    }

    fun setDocumentUri(uri: Uri) {
        _directoryUri.value = Event(uri)
    }

    fun saveRootUri(uri: String) {
        Completable.create {
            repository.saveRootUri(uri)
            it.onComplete()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(compositeDisposable)
    }

    fun loadRootUri() {
        repository.loadRootUri()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                setDocumentUri(Uri.parse(it))
            }
            .addTo(compositeDisposable)
    }

    private fun addBreadCrumbs(uri: Uri) {
        breadCrumbsStack.push(uri)
    }

    fun backBreadCrumbs() {
        if (breadCrumbsStack.isNotEmpty()) {
            setDocumentUri(breadCrumbsStack.pop())
        } else {
            _isBackBreadCrumbsEmpty.value = Event(true)
        }
    }
}