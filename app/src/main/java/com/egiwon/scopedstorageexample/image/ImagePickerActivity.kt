package com.egiwon.scopedstorageexample.image

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.egiwon.scopedstorageexample.R
import com.egiwon.scopedstorageexample.base.BaseActivity
import com.egiwon.scopedstorageexample.databinding.ActivityImagePickerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePickerActivity : BaseActivity<ActivityImagePickerBinding, ImagePickerViewModel>(
    R.layout.activity_image_picker
) {
    override val viewModel: ImagePickerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind { initBinding() }

        loadImagesAfterCheckPermission()
    }

    private fun loadImagesAfterCheckPermission() {
        if (checkMediaPermission()) {
            loadGalleryImages()
        } else {
            requestMediaStoreAccessPermissions()
        }
    }

    private fun requestMediaStoreAccessPermissions() {
        requestPermissions(arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE),
            MEDIA_ACCESS_PERMISSION
        )
    }

    private fun checkMediaPermission(): Boolean {
        val isPermissionGranted: Int = ContextCompat
            .checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        return isPermissionGranted == PackageManager.PERMISSION_GRANTED
    }

    private fun ActivityImagePickerBinding.initBinding() {
        lifecycleOwner = this@ImagePickerActivity
        vm = viewModel
        swipeContainer.setOnRefreshListener(::loadGalleryImages)
        run(::initAdapter)
    }

    private fun initAdapter() {
        binding.rvImageList.apply {
            adapter = ImagePickerAdapter(viewModel)
            setHasFixedSize(true)
        }
    }

    private fun loadGalleryImages() {
        viewModel.loadImages()
    }

    companion object {
        private const val MEDIA_ACCESS_PERMISSION = 0xacce
    }

}