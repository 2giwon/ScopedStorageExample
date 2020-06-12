package com.egiwon.scopedstorageexample.di

import com.egiwon.scopedstorageexample.filebrowser.FileBrowserViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { FileBrowserViewModel(androidApplication()) }
}