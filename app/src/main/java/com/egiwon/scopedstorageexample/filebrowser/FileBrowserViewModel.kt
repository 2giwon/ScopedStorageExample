package com.egiwon.scopedstorageexample.filebrowser

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.egiwon.scopedstorageexample.base.BaseViewModel

class FileBrowserViewModel : BaseViewModel() {

    private val _directoryUri = MutableLiveData<Uri>()
    val directoryUri: LiveData<Uri> get() = _directoryUri

    fun setDirectoryUri(directoryUri: Uri) {
        _directoryUri.value = directoryUri
    }


}