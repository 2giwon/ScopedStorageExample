package com.egiwon.scopedstorageexample.ext

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}