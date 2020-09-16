package com.example.hotellistapp.ui.fragmnet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotellistapp.R
import com.example.hotellistapp.adapter.RoomsListAdapter
import com.example.hotellistapp.api.ApiManager
import com.example.hotellistapp.api.RoomsResponse
import com.example.hotellistapp.model.ProductInfos
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response

class RoomsFragment : BaseFragment() {
    private lateinit var compositeDisposable: CompositeDisposable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_rooms, container, false)

        return view
    }

    override fun onResume() {
        super.onResume()
        init()
    }
    private fun init() {
        var listItems = ArrayList<ProductInfos>()

        compositeDisposable = CompositeDisposable()

        val process = Single.just(1)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .flatMap { getOneList() }
            .doOnSuccess {
                for(item in it.data.product) {
                    listItems.add(ProductInfos(item.name, item.image, item.info.imgPath,item.info.subject, item.info.price, item.rate))
                }
            }
            .flatMap { getTwoList() }
            .doOnSuccess {
                for(item in it.data.product) {
                    listItems.add(ProductInfos(item.name, item.image, item.info.imgPath,item.info.subject, item.info.price, item.rate))
                }
            }
            .flatMap { getThreeList() }
            .doOnSuccess {
                for(item in it.data.product) {
                    listItems.add(ProductInfos(item.name, item.image, item.info.imgPath,item.info.subject, item.info.price, item.rate))
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val roomsListAdapter = RoomsListAdapter(activity!!, listItems)

                val roomsRecyclerView = view?.findViewById<RecyclerView>(R.id.rooms_recycler_view)

                roomsListAdapter.let {
                    roomsRecyclerView?.adapter = roomsListAdapter
                    val layoutManger = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                    roomsRecyclerView?.layoutManager = layoutManger
                }
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
    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}