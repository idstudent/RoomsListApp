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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotellistapp.R
import com.example.hotellistapp.adapter.RoomsListAdapter
import com.example.hotellistapp.db.entity.RoomsRememberEntity
import com.example.hotellistapp.listener.ItemClickListener
import com.example.hotellistapp.model.ProductInfos
import com.example.hotellistapp.viewModel.RoomsViewModel
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
    private var rememberList = ArrayList<ProductInfos>()
    private var pageCheck = 1
    private var call = false

    private val viewModel : RoomsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }
    private fun init() {
        compositeDisposable = CompositeDisposable()
        pageCheck = 1
        likeCheck()

        val progressBar = view?.findViewById<ProgressBar>(R.id.progress)

        roomsListAdapter = RoomsListAdapter(requireActivity(), rememberList, "list")
        roomsListAdapter.rememberListener(rememberListener)

        val roomsRecyclerView = view?.findViewById<RecyclerView>(R.id.rooms_recycler_view)
        roomsRecyclerView?.adapter = roomsListAdapter

        val layoutManger = LinearLayoutManager(activity)
        roomsRecyclerView?.layoutManager = layoutManger

        viewModel.roomsGetList(1)

        viewModel.apply {
            // 프래그먼트 생명주기때문에 this가 아닌 viewLifecycleOwner을 써줘야된다고함
            // http://pluu.github.io/blog/android/2020/01/25/android-fragment-lifecycle/
            viewModel.itemLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                roomsListAdapter.updateItems(it)

            })
            loadingLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                progressBar?.visibility = if(it) View.VISIBLE else View.GONE

                roomsRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if(!roomsRecyclerView.canScrollVertically(1) && pageCheck < viewModel.totalCount) {
                            progressBar?.visibility = View.VISIBLE

                            // 엄청 빠르게 스크롤시 연달아 호출해서 리스트 1개가 안나옴, 그래서 call로 체크해서 이를 방지
                            if(!call) {
                                pageCheck++
                                Handler(Looper.getMainLooper()).postDelayed({
                                    roomsGetList(pageCheck)
                                }, 1500)
                                call = true
                            }else {
                                call = false
                            }
                        }
                    }
                })
            })
        }

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