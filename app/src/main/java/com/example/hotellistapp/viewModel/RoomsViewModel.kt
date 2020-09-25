package com.example.hotellistapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotellistapp.api.ApiManager
import com.example.hotellistapp.model.ProductInfos
import kotlinx.coroutines.launch

class RoomsViewModel : ViewModel() {
    val itemLiveData = MutableLiveData<ArrayList<ProductInfos>>()
    val itemList = ArrayList<ProductInfos>()
    val loadingLiveData = MutableLiveData<Boolean>()
    var totalCount : Int = 0

    fun roomsGetList(i: Int) {
        loadingLiveData.value = true
        viewModelScope.launch {
            val productResponse =  ApiManager.getInstance().getRoomsList("$i.json")
            val productInfosObject = productResponse.asJsonObject
            val dataObject = productInfosObject["data"].asJsonObject
            val productArray = dataObject["product"].asJsonArray
            productArray.forEach {
                val productObject = it.asJsonObject
                val descriptionObject = productObject["description"].asJsonObject

                val id = productObject["id"].asInt
                val name = productObject["name"].asString
                val thumbnail = productObject["thumbnail"].asString
                val rate = productObject["rate"].asDouble
                val imagePath = descriptionObject["imagePath"].asString
                val subject = descriptionObject["subject"].asString
                val price = descriptionObject["price"].asInt

                itemList.add(ProductInfos(id,name,thumbnail,imagePath,subject,price,rate))
            }
            totalCount = dataObject["totalCount"].asInt
            if(totalCount % 20 == 0){
                totalCount /= 20
            } else {
                totalCount = totalCount/20 + 1
            }
            itemLiveData.value = itemList

            loadingLiveData.value = false
        }
    }
}