package com.egiwon.scopedstorageexample.filebrowser

import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egiwon.scopedstorageexample.Event
import com.egiwon.scopedstorageexample.base.BaseViewModel
import com.egiwon.scopedstorageexample.ext.toFileSizeUnit
import com.egiwon.scopedstorageexample.ext.toLastModifiedTime
import com.egiwon.scopedstorageexample.model.DocumentItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

class FileBrowserViewModel(application: Application) : BaseViewModel(application) {

    private val _directoryUri = MutableLiveData<Event<Uri>>()
    val directoryUri: LiveData<Event<Uri>> get() = _directoryUri

    private val _documents = MutableLiveData<List<DocumentItem>>()
    val documents: LiveData<List<DocumentItem>> get() = _documents

    private val _loadingBar = MutableLiveData<Boolean>()
    val loadingBar: LiveData<Boolean> get() = _loadingBar

    private val _documentList = mutableListOf<DocumentItem>()

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
                val resultList = loadDirectoryFromContentResolver(documentUri)
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

    private fun loadDirectoryFromContentResolver(documentUri: Uri): List<DocumentItem> {

        val treeDocumentUri: Uri = getTreeDocumentUri(documentUri)

        val contentResolver: ContentResolver = getApplication<Application>().contentResolver
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            treeDocumentUri,
            DocumentsContract.getDocumentId(treeDocumentUri)
        )

        val result = mutableListOf<DocumentItem>()
        var cursor: Cursor? = null

        try {
            cursor = createCursor(contentResolver, childrenUri)

            cursor?.let { c ->
                while (c.moveToNext()) {
                    val documentId = c.getDocumentID()
                    val name = c.getDocumentName()
                    val type = c.getDocumentType()
                    val isDirectory: Boolean = type == DocumentsContract.Document.MIME_TYPE_DIR
                    val lastModified = c.getLastModified().toLastModifiedTime()
                    val uri = getDocumentUri(treeDocumentUri, documentId)
                    val size = c.getDocumentSize().toFileSizeUnit()

                    result.addDocumentItem(name, type, isDirectory, uri, size, lastModified)
                }

            }
        } catch (e: Exception) {
            throw e
        } finally {
            closeQuietly(cursor)
        }

        return result

    }

    private fun MutableList<DocumentItem>.addDocumentItem(
        name: String?,
        type: String?,
        isDirectory: Boolean,
        uri: Uri,
        size: String,
        lastModified: String
    ) {
        add(
            DocumentItem(
                name ?: "",
                type ?: "",
                isDirectory,
                uri,
                size,
                lastModified)
        )
    }

    private fun createCursor(contentResolver: ContentResolver, childrenUri: Uri): Cursor? {
        return contentResolver.query(
            childrenUri,
            arrayOf(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
                DocumentsContract.Document.COLUMN_SIZE
            ), null, null, null
        )
    }

    private fun Cursor.getDocumentSize(): Long =
        getLong(getColumnIndex(DocumentsContract.Document.COLUMN_SIZE))

    private fun Cursor.getDocumentID(): String? =
        getString(getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID))

    private fun Cursor.getDocumentName(): String? =
        getString(getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME))

    private fun Cursor.getDocumentType(): String? =
        getString(getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE))

    private fun Cursor.getLastModified(): Long =
        getLong(getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED))

    private fun getTreeDocumentUri(documentUri: Uri): Uri {
        var treeDocumentId = DocumentsContract.getTreeDocumentId(documentUri)
        if (DocumentsContract.isDocumentUri(getApplication(), documentUri)) {
            treeDocumentId = DocumentsContract.getDocumentId(documentUri)
        }

        return getDocumentUri(documentUri, treeDocumentId)
    }

    private fun getDocumentUri(treeDocumentUri: Uri, documentId: String?) =
        DocumentsContract.buildDocumentUriUsingTree(treeDocumentUri, documentId)

    private fun closeQuietly(closeable: AutoCloseable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (rethrown: RuntimeException) {
                throw rethrown
            } catch (ignored: java.lang.Exception) {
            }
        }
    }

    fun setDocumentUri(uri: Uri) {
        _directoryUri.value = Event(uri)
    }

    fun saveRootUri(uri: String) {
//        Completable.create {
//            repository.saveRootUri(uri)
//            it.onComplete()
//        }
//            .subscribeOn(ioThreadSchedulers)
//            .observeOn(mainThreadSchedulers)
//            .subscribe()
//            .addTo(compositeDisposable)
    }

    fun loadRootUri() {
//        repository.loadRootUri()
//            .subscribeOn(ioThreadSchedulers)
//            .observeOn(mainThreadSchedulers)
//            .subscribeBy {
//                setDocumentUri(Uri.parse(it))
//            }
//            .addTo(compositeDisposable)
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