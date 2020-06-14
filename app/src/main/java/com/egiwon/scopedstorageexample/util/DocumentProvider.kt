package com.egiwon.scopedstorageexample.util

import android.net.Uri
import com.egiwon.scopedstorageexample.model.DocumentItem

interface DocumentProvider {

    fun getDocuments(documentUri: Uri): List<DocumentItem>
}