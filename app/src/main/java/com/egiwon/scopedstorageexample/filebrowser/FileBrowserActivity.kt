package com.egiwon.scopedstorageexample.filebrowser

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.egiwon.scopedstorageexample.R
import com.egiwon.scopedstorageexample.base.BaseActivity
import com.egiwon.scopedstorageexample.databinding.ActivityFileBrowserBinding
import com.egiwon.scopedstorageexample.model.DocumentItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FileBrowserActivity : BaseActivity<ActivityFileBrowserBinding, FileBrowserViewModel>(
    R.layout.activity_file_browser
) {

    override val viewModel: FileBrowserViewModel by viewModel()

    private val behaviorSubject = BehaviorSubject.createDefault(0L)

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind {
            vm = viewModel
            initAdapter()
        }

        openIntentDirectory()
        addObserve()

        processBackPressedExit()
    }

    override fun addObserve() {
        viewModel.directoryUri.observe(this, Observer { event ->
            event.getContentIfNotHandled().let {
                viewModel.loadDirectory(it ?: return@Observer)
            }
        })

        viewModel.directoryLiveData.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { document ->
                viewModel.setDocumentUri(document.uri)
            }
        })

        viewModel.documentLiveData.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { document ->
                openDocument(document)
            }
        })

        viewModel.isBackBreadCrumbsEmpty.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                behaviorSubject.onNext(System.currentTimeMillis())
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
        if (OPEN_DIRECTORY_REQUEST_CODE == requestCode) {
            if (resultCode == RESULT_OK) {
                val directoryUri = data?.data ?: return

                contentResolver.takePersistableUriPermission(
                    directoryUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                viewModel.setDirectoryUri(directoryUri)
            } else {
                finish()
            }

        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun openIntentDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }

        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
    }

    override fun onBackPressed() {
        viewModel.backBreadCrumbs()
    }

    private fun processBackPressedExit() {
        behaviorSubject.buffer(2, 1)
            .map { it[0] to it[1] }
            .subscribe {
                if (it.second - it.first < 2000L) {
                    super.onBackPressed()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.back_press_exit),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addTo(compositeDisposable)
    }

    private fun openDocument(item: DocumentItem) {
        try {
            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                data = item.uri
            }
            startActivity(openIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this,
                resources.getString(R.string.error_invalid_activity, item.name),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val OPEN_DIRECTORY_REQUEST_CODE = 0xf11e
    }
}