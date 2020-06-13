package com.egiwon.scopedstorageexample.di

import com.egiwon.scopedstorageexample.data.source.preference.FileBrowserPreferenceService
import com.egiwon.scopedstorageexample.data.source.preference.PreferenceService
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val preferenceModule = module {
    single<PreferenceService> { FileBrowserPreferenceService(androidApplication()) }
}