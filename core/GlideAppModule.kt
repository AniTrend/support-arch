package io.wax911.sample.config

import android.content.Context
import co.anitrend.arch.extension.getCompatDrawable
import co.anitrend.arch.extension.isLowRamDevice
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import io.wax911.sample.R

@GlideModule
class GlideAppModule : AppGlideModule() {

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val isLowRamDevice = context.isLowRamDevice()

        val calculator = MemorySizeCalculator.Builder(context)
            .setMemoryCacheScreens((if (isLowRamDevice) 1 else 2).toFloat()).build()

        // Increasing cache & pool by 25% - default is 250MB
        val memoryCacheSize = (1.25 * calculator.memoryCacheSize).toInt()
        val bitmapPoolSize = (1.25 * calculator.bitmapPoolSize).toInt()
        val storageCacheSize = context.externalCacheDir?.let {
            (it.totalSpace * 0.2).toInt()
        } ?: 1024 * 1024 * 350

        builder.setMemoryCache(LruResourceCache(memoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(bitmapPoolSize.toLong()))
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, storageCacheSize.toLong()))

        // Setting default params for glide
        val options = RequestOptions()
            .format(if (isLowRamDevice) DecodeFormat.PREFER_RGB_565 else DecodeFormat.PREFER_ARGB_8888)
            .timeout(GLIDE_IMAGE_REQUEST_TIME_OUT)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(context.getCompatDrawable(R.drawable.ic_support_empty_state))

        builder.setDefaultRequestOptions(options)
    }

    companion object {
        const val GLIDE_IMAGE_REQUEST_TIME_OUT = 5000
    }
}