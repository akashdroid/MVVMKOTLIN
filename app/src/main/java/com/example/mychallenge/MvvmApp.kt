package com.example.mychallenge

import android.app.Activity
import android.app.Application
import com.androidnetworking.AndroidNetworking


import javax.inject.Inject

import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import okhttp3.logging.HttpLoggingInterceptor
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class MvvmApp : Application(), HasActivityInjector {

    @Inject
    internal var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>? = null

    @Inject
    internal var mCalligraphyConfig: CalligraphyConfig? = null

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return activityDispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        AppLogger.init()

        AndroidNetworking.initialize(applicationContext)
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY)
        }

        CalligraphyConfig.initDefault(mCalligraphyConfig)
    }
}
