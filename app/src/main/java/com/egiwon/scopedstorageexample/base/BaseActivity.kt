package com.egiwon.scopedstorageexample.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer

abstract class BaseActivity<VDB : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes private val layoutResId: Int
) : AppCompatActivity() {

    protected abstract val viewModel: VM

    protected lateinit var binding: VDB
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this
        addObserve()
    }

    protected open fun addObserve() {
        viewModel.errorThrowableLiveData.observe(this, Observer {
            showToast(it.message ?: return@Observer)
        })
    }

    protected fun bind(action: VDB.() -> Unit) {
        binding.run(action)
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    protected fun showToast(textResId: Int) {
        showToast(getString(textResId))
    }
}