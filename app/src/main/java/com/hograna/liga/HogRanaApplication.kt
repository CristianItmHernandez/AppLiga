package com.hograna.liga

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HogRanaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val maxHeapBytes = Runtime.getRuntime().maxMemory()
        val cacheSizeBytes = (maxHeapBytes * 0.15)
            .toLong()
            .coerceAtMost(Int.MAX_VALUE.toLong())
            .toInt()

        val imageLoader = ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizeBytes(cacheSizeBytes)
                    .build()
            }
            .diskCache {
                coil.disk.DiskCache.Builder()
                    .directory(cacheDir.resolve("coil_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .crossfade(true)
            .build()

        Coil.setImageLoader(imageLoader)
    }
}
