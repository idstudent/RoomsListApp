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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotellistapp.R
import com.example.hotellistapp.adapter.RoomsListAdapter
import com.example.hotellistapp.listener.ItemClickListener
import com.example.hotellistapp.model.ProductInfos
import com.example.hotellistapp.viewModel.LikeRoomsViewModel
import com.example.hotellistapp.viewModel.RoomsViewModel
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
    private var pos = -1
    private val viewModel : LikeRoomsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rooms_like, container, false)
    }

    override fun onResume() {
        super.onResume()
        init()
        setSpinner()
    }
    private fun init() {

        compositeDisposable = CompositeDisposable()
        rememberRecyclerView = view?.findViewById(R.id.remember_list)!!
        roomsListAdapter = RoomsListAdapter(requireActivity(), listItems, "remember")
        roomsListAdapter.rememberListener(deleteListener)
        rememberRecyclerView.adapter = roomsListAdapter
        rememberRecyclerView.layoutManager = LinearLayoutManager(activity)

        spinner = view?.findViewById(R.id.sort_spinner)!!

        sortSpinner = ArrayAdapter.createFromResource(requireContext(), R.array.sort_array, R.layout.support_simple_spinner_dropdown_item)
        sortSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = sortSpinner

        viewModel.apply {
            viewModel.selectLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                roomsListAdapter.updateItems(it)
            })
            this.latelySelect()
        }
    }

    private val deleteListener = object : ItemClickListener {
        override fun onClick(item: ProductInfos) {
            viewModel.deleteList(item, pos)
            Toast.makeText(context, R.string.delete_text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setSpinner() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                pos = position

                if(sortSpinner.getItem(position) == "등록순") {
                    viewModel.latelySelect()
                }else if(sortSpinner.getItem(position) == "평점 높은순"){
                    viewModel.rateDESCSelect()
                }else {
                    viewModel.rateASCSelect()
                }
            }
        }
    }
}