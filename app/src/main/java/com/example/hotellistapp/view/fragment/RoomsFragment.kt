package com.example.hotellistapp.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotellistapp.R
import com.example.hotellistapp.view.adapter.RoomsListAdapter
import com.example.hotellistapp.listener.ItemClickListener
import com.example.hotellistapp.model.ProductInfos
import com.example.hotellistapp.viewModel.RoomsViewModel

class RoomsFragment : BaseFragment() {
    private lateinit var roomsListAdapter: RoomsListAdapter
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

    override fun onResume() {
        super.onResume()
        viewModel.likeCheck()

    }
    private fun init() {
        pageCheck = 1

        val progressBar = view?.findViewById<ProgressBar>(R.id.progress)

        roomsListAdapter = RoomsListAdapter(requireActivity(),"list")
        roomsListAdapter.rememberListener(rememberListener)

        val roomsRecyclerView = view?.findViewById<RecyclerView>(R.id.rooms_recycler_view)
        roomsRecyclerView?.adapter = roomsListAdapter

        val layoutManger = LinearLayoutManager(activity)
        roomsRecyclerView?.layoutManager = layoutManger

        viewModel.roomsGetList(1)

        viewModel.apply {
            // 프래그먼트 생명주기때문에 this가 아닌 viewLifecycleOwner을 써줘야된다고함
            // this쓰면 호출2번해서 리스트가 2개나옴
            // http://pluu.github.io/blog/android/2020/01/25/android-fragment-lifecycle/
            viewModel.getRememberItemLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                roomsListAdapter.rememberItems(it)
            })
            viewModel.getItemLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                roomsListAdapter.updateItems(it)

            })
            getLoadingLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                progressBar?.visibility = if(it) View.VISIBLE else View.GONE

                roomsRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if(!roomsRecyclerView.canScrollVertically(1) && pageCheck < viewModel.getTotalCount()) {
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
                viewModel.insert(item)
                Toast.makeText(activity, R.string.insert_text, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.delete(item)
                Toast.makeText(activity, R.string.delete_text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}