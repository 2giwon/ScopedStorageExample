package com.egiwon.scopedstorageexample

import android.app.Application
import com.egiwon.scopedstorageexample.di.dataSourceModule
import com.egiwon.scopedstorageexample.di.preferenceModule
import com.egiwon.scopedstorageexample.di.repositoryModule
import com.egiwon.scopedstorageexample.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger

class ScopedStorageExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            logger((if (BuildConfig.DEBUG) AndroidLogger() else EmptyLogger()))
            androidContext(this@ScopedStorageExampleApplication)
            modules(
                viewModelModule, dataSourceModule, repositoryModule, preferenceModule
            )
        }
    }
}