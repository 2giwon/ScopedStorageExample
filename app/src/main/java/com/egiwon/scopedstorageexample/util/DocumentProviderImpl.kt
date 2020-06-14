package com.egiwon.scopedstorageexample.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import com.egiwon.scopedstorageexample.ext.toFileSizeUnit
import com.egiwon.scopedstorageexample.ext.toLastModifiedTime
import com.egiwon.scopedstorageexample.model.DocumentItem

class DocumentProviderImpl(private val applicationContext: Context) : DocumentProvider {
    override fun getDocuments(documentUri: Uri): List<DocumentItem> {
        return loadDirectoryFromContentResolver(documentUri)
    }

    private fun loadDirectoryFromContentResolver(documentUri: Uri): List<DocumentItem> {

        val treeDocumentUri: Uri = getTreeDocumentUri(documentUri)

        val contentResolver: ContentResolver = applicationContext.contentResolver
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            treeDocumentUri,
            DocumentsContract.getDocumentId(treeDocumentUri)
        )

        val result = mutableListOf<DocumentItem>()
        var cursor: Cursor? = null

        try {
            cursor = createCursor(contentResolver, childrenUri)

            cursor?.let { c ->
                while (c.moveToNext()) {
                    val documentId = c.getDocumentID()
                    val name = c.getDocumentName()
                    val type = c.getDocumentType()
                    val isDirectory: Boolean = type == DocumentsContract.Document.MIME_TYPE_DIR
                    val lastModified = c.getLastModified().toLastModifiedTime()
                    val uri = getDocumentUri(treeDocumentUri, documentId)
                    val size = c.getDocumentSize().toFileSizeUnit()

                    result.addDocumentItem(name, type, isDirectory, uri, size, lastModified)
                }

            }
        } catch (e: Exception) {
            throw e
        } finally {
            closeQuietly(cursor)
        }

        return result

    }

    private fun MutableList<DocumentItem>.addDocumentItem(
        name: String?,
        type: String?,
        isDirectory: Boolean,
        uri: Uri,
        size: String,
        lastModified: String
    ) {
        add(
            DocumentItem(
                name ?: "",
                type ?: "",
                isDirectory,
                uri,
                size,
                lastModified
            )
        )
    }

    private fun createCursor(contentResolver: ContentResolver, childrenUri: Uri): Cursor? {
        return contentResolver.query(
            childrenUri,
            arrayOf(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
                DocumentsContract.Document.COLUMN_SIZE
            ), null, null, null
        )
    }

    private fun Cursor.getDocumentSize(): Long =
        getLong(getColumnIndex(DocumentsContract.Document.COLUMN_SIZE))

    private fun Cursor.getDocumentID(): String? =
        getString(getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID))

    private fun Cursor.getDocumentName(): String? =
        getString(getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME))

    private fun Cursor.getDocumentType(): String? =
        getString(getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE))

    private fun Cursor.getLastModified(): Long =
        getLong(getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED))

    private fun getTreeDocumentUri(documentUri: Uri): Uri {
        var treeDocumentId = DocumentsContract.getTreeDocumentId(documentUri)
        if (DocumentsContract.isDocumentUri(applicationContext, documentUri)) {
            treeDocumentId = DocumentsContract.getDocumentId(documentUri)
        }

        return getDocumentUri(documentUri, treeDocumentId)
    }

    private fun getDocumentUri(treeDocumentUri: Uri, documentId: String?) =
        DocumentsContract.buildDocumentUriUsingTree(treeDocumentUri, documentId)

    private fun closeQuietly(closeable: AutoCloseable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (rethrown: RuntimeException) {
                throw rethrown
            } catch (ignored: java.lang.Exception) {
            }
        }
    }
}