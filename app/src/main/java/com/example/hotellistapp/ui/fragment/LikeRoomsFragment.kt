package com.example.hotellistapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
    private lateinit var spinner : Spinner
    private lateinit var sortSpinner : ArrayAdapter<CharSequence>
    private lateinit var rememberRecyclerView : RecyclerView
    private val checkList = ArrayList<ProductInfos>()
    private var pos = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rooms_like, container, false)
    }

    override fun onResume() {
        super.onResume()

        compositeDisposable = CompositeDisposable()
        rememberRecyclerView = view?.findViewById(R.id.remember_list)!!
        roomsListAdapter = RoomsListAdapter(activity!!, listItems, checkList, "remember")
        roomsListAdapter.rememberListener(deleteListener)
        rememberRecyclerView.adapter = roomsListAdapter
        rememberRecyclerView.layoutManager = LinearLayoutManager(activity)

        spinner = view?.findViewById(R.id.sort_spinner)!!

        sortSpinner = ArrayAdapter.createFromResource(requireContext(), R.array.sort_array, R.layout.support_simple_spinner_dropdown_item)
        sortSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = sortSpinner

        getRememberList()
        setSpinner()
    }
    private fun getRememberList() {
        latelyAddSelect()
    }
    private fun latelyAddSelect() {

        listItems.clear()

        compositeDisposable.add(
            dbManager.roomsRememberDAO().latelyAddSelect()
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

                    roomsListAdapter.notifyDataSetChanged()
                }, {

                })
        )
    }
    private fun rateASCSelect() {
        listItems.clear()

        compositeDisposable.add(
            dbManager.roomsRememberDAO().rateASC()
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
                    roomsListAdapter.notifyDataSetChanged()
                }, {}
                )
        )
    }
    private fun rateDESCSelect() {
        listItems.clear()

        compositeDisposable.add(
            dbManager.roomsRememberDAO().rateDESC()
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
                    roomsListAdapter.notifyDataSetChanged()
                }, {}
                )
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
            dbManager.roomsRememberDAO().latelyAddSelect()
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
    private fun setSpinner() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                pos = position

                if(sortSpinner.getItem(position) == "등록순") {
                    latelyAddSelect()
                }else if(sortSpinner.getItem(position) == "평점 높은순"){
                    rateDESCSelect()
                }else {
                    rateASCSelect()
                }
            }
        }
    }
}