package com.example.hotellistapp.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotellistapp.R
import com.example.hotellistapp.api.ApiManager
import com.example.hotellistapp.db.DBManager
import com.example.hotellistapp.db.entity.RoomsRememberEntity
import com.example.hotellistapp.model.ProductInfos
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RoomsViewModel(application: Application) : AndroidViewModel(application) {

    val itemLiveData = MutableLiveData<ArrayList<ProductInfos>>()
    private val itemList = ArrayList<ProductInfos>()
    val loadingLiveData = MutableLiveData<Boolean>()
    var totalCount : Int = 0
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val dbManager = DBManager.getInstance(application)

    fun insert(item : ProductInfos) {
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var curTime = dateFormat.format(Date(time))
        val insert = Observable.just(
            RoomsRememberEntity(
                id = item.id,
                name = item.name,
                thumbnail = item.thumbnail,
                imgPath = item.imgUrl,
                subject = item.subject,
                price = item.price,
                rate = item.rate,
                time = curTime,
                check = item.check
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnNext {
                dbManager?.roomsRememberDAO()?.insert(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {}
        compositeDisposable.add(insert)
    }
    fun delete(item : ProductInfos) {
        compositeDisposable.add(
            Observable.fromCallable {
                dbManager?.roomsRememberDAO()?.deleteItem(item.id)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {}
        )
    }
    fun roomsGetList(i: Int) {
        loadingLiveData.value = true

        //코루틴
        viewModelScope.launch {
            val productResponse =  ApiManager.getInstance().getRoomsList("$i.json")
            val productInfosObject = productResponse.asJsonObject
            val dataObject = productInfosObject["data"].asJsonObject
            val productArray = dataObject["product"].asJsonArray
            productArray.forEach {
                val productObject = it.asJsonObject
                val descriptionObject = productObject["description"].asJsonObject

                val id = productObject["id"].asInt
                val name = productObject["name"].asString
                val thumbnail = productObject["thumbnail"].asString
                val rate = productObject["rate"].asDouble
                val imagePath = descriptionObject["imagePath"].asString
                val subject = descriptionObject["subject"].asString
                val price = descriptionObject["price"].asInt

                itemList.add(ProductInfos(id,name,thumbnail,imagePath,subject,price,rate))
            }
            totalCount = dataObject["totalCount"].asInt
            if(totalCount % 20 == 0){
                totalCount /= 20
            } else {
                totalCount = totalCount/20 + 1
            }
            itemLiveData.value = itemList

            loadingLiveData.value = false
        }
    }
}