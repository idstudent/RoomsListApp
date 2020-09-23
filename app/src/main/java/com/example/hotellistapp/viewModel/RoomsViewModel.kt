package com.example.hotellistapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotellistapp.api.ApiManager
import com.example.hotellistapp.api.ApiService
import com.example.hotellistapp.api.RoomsResponse
import kotlinx.coroutines.launch

class RoomsViewModel : ViewModel() {
    val itemLiveData = MutableLiveData<ArrayList<List<RoomsResponse.Data.Product>>>()
    val itemList = ArrayList<List<RoomsResponse.Data.Product>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    var totalCount : Int = 0
    val index : Int = 1

    init {
        roomsGetList(index)
    }
    fun roomsGetList(i: Int) {
        loadingLiveData.value = true

        viewModelScope.launch {
            val productInfos =  ApiManager.getInstance().getRoomsList("$i.json")
            itemList.add(productInfos.data.product)
            itemLiveData.value = itemList
            totalCount = productInfos.data.cnt
            loadingLiveData.value = false
        }
    }
}