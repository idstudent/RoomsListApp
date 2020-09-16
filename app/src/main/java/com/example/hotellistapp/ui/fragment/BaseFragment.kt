package com.example.hotellistapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.hotellistapp.db.DBManager
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment() {
    protected lateinit var dbManager: DBManager

    private val compositeDisposable : CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbManager = activity?.let { DBManager.getInstance(it) } ?: return
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}