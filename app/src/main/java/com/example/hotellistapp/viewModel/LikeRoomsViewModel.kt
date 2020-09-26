package com.example.hotellistapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.hotellistapp.db.DBManager
import com.example.hotellistapp.model.ProductInfos
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LikeRoomsViewModel(application: Application) : AndroidViewModel(application) {
    private var listItems = ArrayList<ProductInfos>()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val dbManager = DBManager.getInstance(application)
    val latelySelectLiveData = MutableLiveData<ArrayList<ProductInfos>>()

    fun select() {
        listItems.clear()

        compositeDisposable.add(
            dbManager?.roomsRememberDAO()?.latelyAddSelect()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe{
                    for (i in it.indices) {
                        listItems.add(
                            ProductInfos(
                                it[i].id, it[i].name, it[i].thumbnail, it[i].imgPath,
                                it[i].subject, it[i].price, it[i].rate, it[i].time, it[i].check
                            )
                        )
                    }
                    latelySelectLiveData.value = listItems
                }!!
        )
    }
}