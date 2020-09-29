package com.example.hotellistapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.hotellistapp.model.db.DBManager
import com.example.hotellistapp.model.ProductInfos
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LikeRoomsViewModel(application: Application) : AndroidViewModel(application) {
    private var listItems = ArrayList<ProductInfos>()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val dbManager = DBManager.getInstance(application)
    val selectLiveData = MutableLiveData<ArrayList<ProductInfos>>()

    fun latelySelect() {
        listItems.clear()

        compositeDisposable.add(
            dbManager?.roomsRememberDAO()?.latelyAddSelect()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe{
                    for (i in it.indices) {
                        listItems.add(
                            ProductInfos(
                                it[i].id, it[i].title, it[i].thumbnail, it[i].imgPath,
                                it[i].subject, it[i].price, it[i].rate, it[i].time, it[i].check
                            )
                        )
                    }
                    selectLiveData.value = listItems
                }!!
        )
    }
    fun rateASCSelect() {
        listItems.clear()

        compositeDisposable.add(
            dbManager?.roomsRememberDAO()?.rateASC()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe{
                    for (i in it.indices) {
                        listItems.add(
                            ProductInfos(
                                it[i].id, it[i].title, it[i].thumbnail, it[i].imgPath,
                                it[i].subject, it[i].price, it[i].rate, it[i].time, it[i].check
                            )
                        )
                    }
                    selectLiveData.value = listItems
                }!!
        )
    }
    fun rateDESCSelect() {
        listItems.clear()

        compositeDisposable.add(
            dbManager?.roomsRememberDAO()?.rateDESC()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe {
                    for (i in it.indices) {
                        listItems.add(
                            ProductInfos(
                                it[i].id, it[i].title, it[i].thumbnail, it[i].imgPath,
                                it[i].subject, it[i].price, it[i].rate, it[i].time, it[i].check
                            )
                        )
                    }
                   selectLiveData.value = listItems
                }!!
        )
    }

    fun deleteList(item : ProductInfos, pos : Int) {
        compositeDisposable.add(
            Observable.fromCallable {
                dbManager?.roomsRememberDAO()?.deleteItem(item.id)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    redrawList(pos)
                },{
                    Log.e("tag", "exception $it")
                })
        )
    }
    fun redrawList(pos: Int) {
        listItems.clear()

        when(pos) {
            0->latelySelect()
            1->rateDESCSelect()
            2->rateASCSelect()
        }
    }
}