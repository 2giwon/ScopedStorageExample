package com.egiwon.scopedstorageexample.data

import io.reactivex.Single

interface FileBrowserRepository {
    fun saveRootUri(uri: String)

    fun loadRootUri(): Single<String>
}