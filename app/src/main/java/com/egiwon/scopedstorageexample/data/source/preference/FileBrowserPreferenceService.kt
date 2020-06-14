package com.egiwon.scopedstorageexample.data.source.preference

import android.content.Context
import io.reactivex.Single
import javax.inject.Inject

class FileBrowserPreferenceService @Inject constructor(
    private val applicationContext: Context
) : PreferenceService {
    override fun saveRootUriToPreference(uri: String) {
        getPreferenceService()
            .edit()
            .putString(FILE_BROWSER_ROOT_URI_KEY, uri)
            .apply()
    }

    override fun loadRootUriFromPreference(): Single<String> {
        return Single.create { emit ->
            runCatching {
                getPreferenceService().getString(FILE_BROWSER_ROOT_URI_KEY, "")
            }.onSuccess {
                emit.onSuccess(it ?: "")
            }.onFailure {
                emit.onError(it)
            }
        }
    }

    private fun getPreferenceService() =
        applicationContext.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)

    companion object {
        private const val PREFERENCE_KEY = "preference_key"
        private const val FILE_BROWSER_ROOT_URI_KEY = "file_browser_root_uri_key"
    }
}