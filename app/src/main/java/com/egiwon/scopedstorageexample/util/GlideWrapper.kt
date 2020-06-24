package com.egiwon.scopedstorageexample.util

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions

object GlideWrapper {
    private fun <T> asyncLoadImage(
        target: ImageView,
        loadContent: T,
        block: RequestOptions.() -> RequestOptions
    ) {
        loadImage(target, loadContent)
            .transitionApply()
            .applyRequestOptions(block)
            .into(target)
    }

    private fun <T> asyncLoadThumbnailImage(
        target: ImageView,
        loadContent: T,
        thumbnailSize: Float
    ) {
        loadImage(target, loadContent)
            .thumbnail(thumbnailSize)
            .transitionApply()
            .into(target)
    }

    private fun <T> loadImage(
        target: ImageView,
        loadContent: T
    ): RequestBuilder<Bitmap> =
        Glide.with(target)
            .asBitmap()
            .load(loadContent)

    private fun RequestBuilder<Bitmap>.transitionApply(): RequestBuilder<Bitmap> {
        return transition(BitmapTransitionOptions.withCrossFade())
            .centerInside()
    }

    private fun RequestBuilder<Bitmap>.applyRequestOptions(
        block: RequestOptions.() -> RequestOptions
    ): RequestBuilder<Bitmap> {
        return apply(RequestOptions().block())
    }

    fun loadThumbnailImage(target: ImageView, uri: String, thumbnailSize: Float) {
        asyncLoadThumbnailImage(target, uri, thumbnailSize)
    }

    fun recycledImage(target: ImageView) {
        Glide.with(target).clear(target)
    }
}