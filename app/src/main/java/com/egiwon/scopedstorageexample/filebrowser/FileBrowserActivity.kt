package com.egiwon.scopedstorageexample.filebrowser

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import com.egiwon.scopedstorageexample.R
import kotlinx.android.synthetic.main.activity_file_browser.*

class FileBrowserActivity : AppCompatActivity(R.layout.activity_file_browser) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        openDocTree()
        rv_files.adapter = FileViewAdapter()
    }

    private fun openDocTree() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            val uri = data?.data ?: return

            contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            loadDirectoryUri(uri)
        }

    }

    private fun loadDirectoryUri(uri: Uri) {
        val documentsTree: DocumentFile =
            DocumentFile.fromTreeUri(applicationContext, uri) ?: return
        val childDocuments: List<DocumentFile> = documentsTree.listFiles().asList()

        (rv_files.adapter as? FileViewAdapter)?.replaceItems(childDocuments)
    }

    companion object {
        const val OPEN_DIRECTORY_REQUEST_CODE = 1
    }

}