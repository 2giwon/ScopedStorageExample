package com.egiwon.scopedstorageexample

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.egiwon.scopedstorageexample.ext.startActivity
import com.egiwon.scopedstorageexample.image.ImagePickerActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    fun startImagePickerExam(view: View) {
        startActivity<ImagePickerActivity>()
    }
}