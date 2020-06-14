package com.egiwon.scopedstorageexample.di

import com.egiwon.scopedstorageexample.data.source.FileBrowserDataSource
import com.egiwon.scopedstorageexample.data.source.local.FileBrowserLocalDataSource
import com.egiwon.scopedstorageexample.data.source.preference.PreferenceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class LocalDataSourceModule {

    @Singleton
    @Provides
    fun provideLocalFileBrowserDataSource(
        preferenceService: PreferenceService
    ): FileBrowserDataSource = FileBrowserLocalDataSource(preferenceService)
}
