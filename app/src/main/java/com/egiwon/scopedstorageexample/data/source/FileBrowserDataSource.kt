package com.egiwon.scopedstorageexample.data.source

import io.reactivex.Single

interface FileBrowserDataSource {
    fun saveRootUri(uri: String)

    fun loadRootUri(): Single<String>
}