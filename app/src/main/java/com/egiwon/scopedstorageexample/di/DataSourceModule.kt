package com.egiwon.scopedstorageexample.di

import com.egiwon.scopedstorageexample.data.FileBrowserRepository
import com.egiwon.scopedstorageexample.data.FileBrowserRepositoryImpl
import com.egiwon.scopedstorageexample.data.source.FileBrowserDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class DataSourceModule {

    @ActivityRetainedScoped
    @Provides
    fun provideFileBrowserRepository(
        fileBrowserDataSource: FileBrowserDataSource
    ): FileBrowserRepository = FileBrowserRepositoryImpl(fileBrowserDataSource)

}