package com.egiwon.scopedstorageexample.data.source.preference

import io.reactivex.Single

interface PreferenceService {
    fun saveRootUriToPreference(uri: String)

    fun loadRootUriFromPreference(): Single<String>
}