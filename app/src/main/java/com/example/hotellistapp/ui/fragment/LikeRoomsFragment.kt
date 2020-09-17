package com.example.hotellistapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotellistapp.R
import com.example.hotellistapp.adapter.RoomsListAdapter
import com.example.hotellistapp.listener.ItemClickListener
import com.example.hotellistapp.model.ProductInfos
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LikeRoomsFragment : BaseFragment() {
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var roomsListAdapter : RoomsListAdapter
    private var listItems = ArrayList<ProductInfos>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rooms_like, container, false)
    }

    override fun onResume() {
        super.onResume()

        getRememberList()
    }
    private fun getRememberList() {
        compositeDisposable = CompositeDisposable()
        listItems.clear()

        val rememberRecyclerView =  view?.findViewById<RecyclerView>(R.id.remember_list)

        val checkList = ArrayList<ProductInfos>()

        compositeDisposable.add(
            dbManager.roomsRememberDAO().select()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    for (i in it.indices) {
                        listItems.add(
                            ProductInfos(
                                it[i].id, it[i].name, it[i].thumbnail, it[i].imgPath,
                                it[i].subject, it[i].price, it[i].rate,it[i].time, it[i].check
                            )
                        )
                    }
                    roomsListAdapter = RoomsListAdapter(activity!!, listItems,checkList,"remember")
                    roomsListAdapter.rememberListener(deleteListener)
                    rememberRecyclerView?.adapter = roomsListAdapter
                    rememberRecyclerView?.layoutManager = LinearLayoutManager(activity)
                    roomsListAdapter.notifyDataSetChanged()
                },{

                })
        )
    }
    private val deleteListener = object : ItemClickListener {
        override fun onClick(item: ProductInfos) {
            deleteList(item)
        }
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
                    redrawList()
                },{
                    Log.e("tag", "exception $it")
                })
        )
    }
    private fun redrawList() {
        listItems.clear()
        compositeDisposable.add(
            dbManager.roomsRememberDAO().select()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    for (i in it.indices) {
                        listItems.add(
                            ProductInfos(
                                it[i].id, it[i].name, it[i].thumbnail, it[i].imgPath,
                                it[i].subject, it[i].price, it[i].rate, it[i].time, it[i].check
                            )
                        )
                    }
                    Toast.makeText(activity,R.string.delete_text, Toast.LENGTH_SHORT).show()
                    roomsListAdapter.notifyDataSetChanged()
                }, {

                })
        )
    }
}