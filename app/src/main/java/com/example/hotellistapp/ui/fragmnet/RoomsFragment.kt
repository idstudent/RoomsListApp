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
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class RoomsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_rooms, container, false)

        return view
    }

    override fun onResume() {
        super.onResume()
        init()
    }
    private fun init() {
        var listItems = ArrayList<String>()
        val roomsListAdapter = RoomsListAdapter(activity!!, listItems)

        for(i in 0..10) {
            listItems.add(i.toString())
        }

        for(i in 1..3) {
            ApiManager.getInstance().getRoomsList("$i.json")
                .enqueue(object : retrofit2.Callback<RoomsResponse> {
                    override fun onFailure(call: Call<RoomsResponse>, t: Throwable) {
                        Log.e("tag", "error " + t)
                    }

                    override fun onResponse(call: Call<RoomsResponse>, response: Response<RoomsResponse>) {
                        if(response.isSuccessful) {
                            if(i==3) {
                                Log.e(
                                    "tag",
                                    "success" + response.body()?.data?.product?.get(2)?.name
                                )
                            }
                        }
                    }

                })
        }



        val roomsRecyclerView = view?.findViewById<RecyclerView>(R.id.rooms_recycler_view)

        roomsListAdapter.let {
            roomsRecyclerView?.adapter = roomsListAdapter
            val layoutManger = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            roomsRecyclerView?.layoutManager = layoutManger
        }

    }
}