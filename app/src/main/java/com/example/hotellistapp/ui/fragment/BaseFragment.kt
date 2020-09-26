package com.example.hotellistapp.ui.fragment

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment() {
    private val compositeDisposable : CompositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}