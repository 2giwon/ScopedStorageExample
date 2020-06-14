package com.egiwon.scopedstorageexample.data

import com.egiwon.scopedstorageexample.data.source.FileBrowserDataSource
import io.reactivex.Single
import javax.inject.Inject

class FileBrowserRepositoryImpl @Inject constructor(
    private val dataSource: FileBrowserDataSource
) : FileBrowserRepository {
    override fun saveRootUri(uri: String) = dataSource.saveRootUri(uri)

    override fun loadRootUri(): Single<String> = dataSource.loadRootUri()

}