package com.egiwon.scopedstorageexample.di

import com.egiwon.scopedstorageexample.data.source.FileBrowserDataSource
import com.egiwon.scopedstorageexample.data.source.local.FileBrowserLocalDataSource
import org.koin.dsl.module

val localDataSourceModule = module {
    single<FileBrowserDataSource> { FileBrowserLocalDataSource(get()) }
}