package com.egiwon.scopedstorageexample.di

import android.content.Context
import com.egiwon.scopedstorageexample.util.image.ImageProvider
import com.egiwon.scopedstorageexample.util.image.ImageProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class ImageProviderModule {

    @ActivityRetainedScoped
    @Provides
    fun provideImageProvider(
        @ApplicationContext applicationContext: Context
    ): ImageProvider = ImageProviderImpl(applicationContext)
}