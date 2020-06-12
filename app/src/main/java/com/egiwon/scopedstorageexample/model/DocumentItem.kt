package com.egiwon.scopedstorageexample.model

import android.net.Uri

data class DocumentItem(
    val name: String,
    val type: String,
    val isDirectory: Boolean,
    val uri: Uri,
    val size: String,
    val lastModified: String
)