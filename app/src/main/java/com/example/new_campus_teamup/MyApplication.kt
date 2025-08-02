package com.example.new_campus_teamup

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.annotation.ExperimentalCoilApi
import coil.disk.DiskCache
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.google.firebase.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() ,ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        Log.d("AppCheck","Application Class onCreate")


        FirebaseApp.initializeApp(this)


        if (BuildConfig.DEBUG) {
            Log.d("AppCheck", "Debug build detected. App Check using Debug provider.")
        }

        // only night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }


    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED) // many option inside this bracket
            .memoryCache{
                MemoryCache.Builder(this)
                    .maxSizePercent(0.1)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache{
                DiskCache.Builder()
                    .maxSizePercent(0.03)
                    .directory(cacheDir)
                    .build()
            }
            .logger(DebugLogger())
            .build()
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun onTerminate() {
        super.onTerminate()
        imageLoader.diskCache?.clear()
        imageLoader.memoryCache?.clear()
    }
}