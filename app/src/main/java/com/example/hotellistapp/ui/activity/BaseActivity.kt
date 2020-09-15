package com.example.hotellistapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.hotellistapp.R
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var layout: CoordinatorLayout
    private lateinit var layoutContainer: FrameLayout

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    override fun setContentView(layoutResID: Int) {
        if(layoutResID != R.layout.activity_main) {
            layout = LayoutInflater.from(this).inflate(R.layout.activity_base, null) as CoordinatorLayout
            layoutContainer = layout.findViewById(R.id.layout_container) as FrameLayout

            LayoutInflater.from(this).inflate(layoutResID, layoutContainer, true)
        }else {

        }
        super.setContentView(layout)
    }
    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
