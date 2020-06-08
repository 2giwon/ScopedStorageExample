package com.egiwon.scopedstorageexample.model

import androidx.documentfile.provider.DocumentFile
import com.egiwon.scopedstorageexample.ext.toFileSizeUnit
import com.egiwon.scopedstorageexample.ext.toLastModifiedTime

data class DocumentItem(private val documentFile: DocumentFile) {
    val name: String? = documentFile.name

    val type: String? by lazy { documentFile.type }

    val size: String? by lazy { documentFile.length().toFileSizeUnit() }

    val lastModified: String? by lazy { documentFile.lastModified().toLastModifiedTime() }

    val isDirectory: Boolean = documentFile.isDirectory
}