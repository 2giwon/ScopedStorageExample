package com.egiwon.scopedstorageexample.di

import android.content.Context
import com.egiwon.scopedstorageexample.util.DocumentProvider
import com.egiwon.scopedstorageexample.util.DocumentProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class DocumentProviderModule {

    @ActivityRetainedScoped
    @Provides
    fun provideDocumentProvider(
        @ApplicationContext applicationContext: Context
    ): DocumentProvider = DocumentProviderImpl(applicationContext)
}