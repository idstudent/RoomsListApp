package com.example.hotellistapp.ui.fragmnet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotellistapp.R
import com.example.hotellistapp.adapter.RoomsListAdapter

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
        val roomsRecyclerView = view?.findViewById<RecyclerView>(R.id.rooms_recycler_view)

        roomsListAdapter.let {
            roomsRecyclerView?.adapter = roomsListAdapter
            val layoutManger = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            roomsRecyclerView?.layoutManager = layoutManger
        }

    }
}