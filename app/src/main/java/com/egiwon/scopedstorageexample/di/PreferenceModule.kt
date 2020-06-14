package com.egiwon.scopedstorageexample.di

import android.content.Context
import com.egiwon.scopedstorageexample.data.source.preference.FileBrowserPreferenceService
import com.egiwon.scopedstorageexample.data.source.preference.PreferenceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class PreferenceModule {

    @Singleton
    @Provides
    fun providesPreferenceService(
        @ApplicationContext applicationContext: Context
    ): PreferenceService =
        FileBrowserPreferenceService(applicationContext)
}
