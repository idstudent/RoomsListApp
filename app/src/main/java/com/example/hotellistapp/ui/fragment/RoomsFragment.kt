package com.example.hotellistapp.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotellistapp.R
import com.example.hotellistapp.adapter.RoomsListAdapter
import com.example.hotellistapp.api.ApiManager
import com.example.hotellistapp.api.RoomsResponse
import com.example.hotellistapp.db.entity.RoomsRememberEntity
import com.example.hotellistapp.listener.ItemClickListener
import com.example.hotellistapp.model.ProductInfos
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class RoomsFragment : BaseFragment() {
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var roomsListAdapter: RoomsListAdapter
    private var listItems = ArrayList<ProductInfos>()
    private var rememberList = ArrayList<ProductInfos>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_rooms, container, false)

        return view
    }

    override fun onResume() {
        super.onResume()
        init()

    }
    private fun init() {
        compositeDisposable = CompositeDisposable()
        likeCheck()

        val progressBar = view?.findViewById<ProgressBar>(R.id.progress)
        progressBar?.visibility = View.VISIBLE

        roomsListAdapter = RoomsListAdapter(activity!!, listItems, rememberList, "list")
        roomsListAdapter.notifyDataSetChanged()


        val process = Single.just(1)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap { getOneList() }
            .doOnSuccess {
                for(item in it.data.product) {
                    listItems.add(ProductInfos(item.id, item.name, item.image, item.info.imgPath,item.info.subject, item.info.price, item.rate))
                }
            }
            .flatMap { getTwoList() }
            .doOnSuccess {
                for(item in it.data.product) {
                    listItems.add(ProductInfos(item.id,item.name, item.image, item.info.imgPath,item.info.subject, item.info.price, item.rate))
                }
            }
            .flatMap { getThreeList() }
            .doOnSuccess {
                for(item in it.data.product) {
                    listItems.add(ProductInfos(item.id, item.name, item.image, item.info.imgPath,item.info.subject, item.info.price, item.rate))
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                progressBar?.visibility = View.GONE
                val roomsRecyclerView = view?.findViewById<RecyclerView>(R.id.rooms_recycler_view)

                roomsRecyclerView?.adapter = roomsListAdapter
                val layoutManger = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                roomsRecyclerView?.layoutManager = layoutManger

                roomsListAdapter.rememberListener(rememberListener)

            },{
            })
        compositeDisposable.add(process)
    }
    private fun getOneList() : Single<RoomsResponse> {
        return ApiManager.getInstance().getRoomsList("1.json")
    }
    private fun getTwoList() : Single<RoomsResponse> {
        return ApiManager.getInstance().getRoomsList("2.json")
    }
    private fun getThreeList() : Single<RoomsResponse> {
        return ApiManager.getInstance().getRoomsList("3.json")
    }

    private val rememberListener = object : ItemClickListener {
        override fun onClick(item: ProductInfos) {
            if(item.check){
                insertList(item)

            }else {
                deleteList(item)
            }
        }
    }

    private fun insertList(item : ProductInfos) {
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
                dbManager.roomsRememberDAO().insert(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                Toast.makeText(activity,R.string.insert_text,Toast.LENGTH_SHORT).show()
            }
        compositeDisposable.add(insert)
    }
    private fun deleteList(item : ProductInfos) {
        compositeDisposable.add(
            Observable.fromCallable {
                dbManager.roomsRememberDAO().deleteItem(item.id)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(activity,R.string.delete_text,Toast.LENGTH_SHORT).show()
                },{
                    Log.e("tag", "exception $it")
                })
        )
    }
    private fun likeCheck() {
        rememberList.clear()
        compositeDisposable.add(
            dbManager.roomsRememberDAO().select()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    for (i in it.indices) {
                        rememberList.add(
                            ProductInfos(
                                it[i].id, it[i].name, it[i].thumbnail, it[i].imgPath,
                                it[i].subject, it[i].price, it[i].rate, it[i].time, it[i].check
                            )
                        )
                    }
                    roomsListAdapter.notifyDataSetChanged()
                }, {

                })
        )
    }
}