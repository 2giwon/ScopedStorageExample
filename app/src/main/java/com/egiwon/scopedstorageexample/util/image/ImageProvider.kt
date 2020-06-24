package com.egiwon.scopedstorageexample.util.image

import com.egiwon.scopedstorageexample.model.MediaStoreImage


interface ImageProvider {
    fun getImages(): List<MediaStoreImage>
}