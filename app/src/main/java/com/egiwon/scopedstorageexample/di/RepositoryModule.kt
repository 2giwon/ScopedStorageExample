package com.egiwon.scopedstorageexample.di

import com.egiwon.scopedstorageexample.data.FileBrowserRepository
import com.egiwon.scopedstorageexample.data.FileBrowserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<FileBrowserRepository> { FileBrowserRepositoryImpl(get()) }
}