package com.egiwon.scopedstorageexample.data.source.local

import com.egiwon.scopedstorageexample.data.source.FileBrowserDataSource
import com.egiwon.scopedstorageexample.data.source.preference.PreferenceService
import io.reactivex.Single
import javax.inject.Inject

class FileBrowserLocalDataSource @Inject constructor(
    private val preferenceService: PreferenceService
) : FileBrowserDataSource {

    override fun saveRootUri(uri: String) = preferenceService.saveRootUriToPreference(uri)

    override fun loadRootUri(): Single<String> = preferenceService.loadRootUriFromPreference()
}