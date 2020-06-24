package com.egiwon.scopedstorageexample.util.image

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.egiwon.scopedstorageexample.model.MediaStoreImage
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ImageProviderImpl @Inject constructor(private val applicationContext: Context) : ImageProvider {
    override fun getImages(): List<MediaStoreImage> {
        val images = mutableListOf<MediaStoreImage>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID
        )
        val selection = "${MediaStore.Images.Media.DATE_ADDED} >= ?"

        val selectionArgs = arrayOf(
            dateToTimestamp(day = 31, month = 12, year = 1970).toString()
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        applicationContext.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                val image = MediaStoreImage(id, MutableLiveData(false), contentUri)
                images += image
            }
        }

        return images
    }

    @Suppress("SameParameterValue")
    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
            TimeUnit.MICROSECONDS.toSeconds(formatter.parse("$day.$month.$year")?.time ?: 0)
        }

}