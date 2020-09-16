package com.example.hotellistapp.util

import android.view.View

fun View.setOnSingleClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SingleClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}