package com.example.hotellistapp.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_rooms.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RoomsFragment : BaseFragment() {
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var roomsListAdapter: RoomsListAdapter
    private var listItems = ArrayList<ProductInfos>()
    private var rememberList = ArrayList<ProductInfos>()
    private var pageCheck = 1
    private var totalPage = 0
    private var call = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rooms, container, false)
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {
        compositeDisposable = CompositeDisposable()
        listItems.clear()
        pageCheck = 1
        likeCheck()

        val progressBar = view?.findViewById<ProgressBar>(R.id.progress)
        progressBar?.visibility = View.VISIBLE

        roomsListAdapter = RoomsListAdapter(activity!!, listItems, rememberList, "list")
        roomsListAdapter.rememberListener(rememberListener)

        val roomsRecyclerView = view?.findViewById<RecyclerView>(R.id.rooms_recycler_view)
        roomsRecyclerView?.adapter = roomsListAdapter

        val layoutManger = LinearLayoutManager(activity)
        roomsRecyclerView?.layoutManager = layoutManger

        roomsGetList(1)

        roomsRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!roomsRecyclerView.canScrollVertically(1) && pageCheck < totalPage) {
                    progressBar?.visibility = View.VISIBLE
                    // 엄청 빠르게 스크롤시 연달아 호출해서 리스트 1개가 안나옴, 그래서 call로 체크해서 이를 방지
                    if(!call) {
                        pageCheck++
                        Handler(Looper.getMainLooper()).postDelayed({
                            roomsGetList(pageCheck)
                        }, 1500)
                    }
                    call = true
                }
            }
        })
    }
    private fun roomsGetList(i : Int) {

        call = false

        ApiManager.getInstance().getRoomsList("$i.json").enqueue(object : retrofit2.Callback<RoomsResponse>{
            override fun onFailure(call: retrofit2.Call<RoomsResponse>, t: Throwable) {
            }

            override fun onResponse(call: retrofit2.Call<RoomsResponse>, response: Response<RoomsResponse>) {
                val totalCount = response.body()?.data?.cnt!!
                if(totalCount % 20 == 0){
                    totalPage = totalCount/20
                } else {
                    totalPage = totalCount/20 + 1
                }

                for(i in 0 until response.body()?.data?.product?.size!!) {
                    listItems.add(ProductInfos(
                        response.body()?.data?.product?.get(i)?.id!!,
                        response.body()?.data?.product?.get(i)?.name!!,
                        response.body()?.data?.product?.get(i)?.image!!,
                        response.body()?.data?.product?.get(i)?.info?.imgPath!!,
                        response.body()?.data?.product?.get(i)?.info?.subject!!,
                        response.body()?.data?.product?.get(i)?.info?.price!!,
                        response.body()?.data?.product?.get(i)?.rate!!))
                }
                progress.visibility = View.GONE
                roomsListAdapter.notifyDataSetChanged()
            }

        })
    }
    private val rememberListener = object : ItemClickListener {
        override fun onClick(item: ProductInfos) {
            if (item.check) {
                insertList(item)

            } else {
                deleteList(item)
            }
        }
    }

    private fun insertList(item: ProductInfos) {
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
            .subscribe {
                Toast.makeText(activity, R.string.insert_text, Toast.LENGTH_SHORT).show()
            }
        compositeDisposable.add(insert)
    }

    private fun deleteList(item: ProductInfos) {
        compositeDisposable.add(
            Observable.fromCallable {
                dbManager.roomsRememberDAO().deleteItem(item.id)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(activity, R.string.delete_text, Toast.LENGTH_SHORT).show()
                }, {
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