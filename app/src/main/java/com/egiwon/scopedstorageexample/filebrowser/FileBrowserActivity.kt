package com.egiwon.scopedstorageexample.filebrowser

import android.content.Intent
import android.os.Bundle
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.egiwon.scopedstorageexample.R
import com.egiwon.scopedstorageexample.base.BaseActivity
import com.egiwon.scopedstorageexample.databinding.ActivityFileBrowserBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FileBrowserActivity : BaseActivity<ActivityFileBrowserBinding, FileBrowserViewModel>(
    R.layout.activity_file_browser
) {

    override val viewModel: FileBrowserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind {
            vm = viewModel
            initAdapter()
        }

        openIntentDirectory()
        addObserve()
    }

    override fun addObserve() {
        viewModel.directoryUri.observe(this, Observer { event ->
            event.getContentIfNotHandled().let {
                val documentFile: DocumentFile =
                    DocumentFile.fromTreeUri(
                        this@FileBrowserActivity,
                        it
                            ?: return@let
                    ) ?: return@Observer
                viewModel.loadDirectory(documentFile)
            }

        })
    }

    private fun ActivityFileBrowserBinding.initAdapter() {
        rvFiles.adapter = FileViewAdapter(viewModel = viewModel)
        rvFiles.setHasFixedSize(true)
        rvFiles.addItemDecoration(
            DividerItemDecoration(this@FileBrowserActivity, DividerItemDecoration.VERTICAL)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (OPEN_DIRECTORY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            val directoryUri = data?.data ?: return

            contentResolver.takePersistableUriPermission(
                directoryUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            viewModel.setDirectoryUri(directoryUri)
        }
    }

    private fun openIntentDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }

        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
    }

    companion object {
        private const val OPEN_DIRECTORY_REQUEST_CODE = 0xf11e
    }
}