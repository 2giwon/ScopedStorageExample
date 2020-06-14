package com.egiwon.scopedstorageexample.di

import com.egiwon.scopedstorageexample.data.FileBrowserRepository
import com.egiwon.scopedstorageexample.data.FileBrowserRepositoryImpl
import com.egiwon.scopedstorageexample.util.DocumentProvider
import com.egiwon.scopedstorageexample.util.DocumentProviderImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataSourceModule = module {
    single<FileBrowserRepository> { FileBrowserRepositoryImpl(get()) }

    single<DocumentProvider> { DocumentProviderImpl(androidApplication()) }
}