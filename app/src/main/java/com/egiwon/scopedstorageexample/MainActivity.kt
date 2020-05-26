package com.egiwon.scopedstorageexample

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.egiwon.scopedstorageexample.ext.startActivity
import com.egiwon.scopedstorageexample.filebrowser.FileBrowserActivity
import com.egiwon.scopedstorageexample.image.ImagePickerActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    fun startImagePickerExam(view: View) {
        startActivity<ImagePickerActivity>()
    }

    fun startFileBrowserExam(view: View) {
        startActivity<FileBrowserActivity>()
    }
}