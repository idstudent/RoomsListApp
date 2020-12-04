package com.example.hotellistapp


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


abstract class ApiCallback<T>(mListener: ResponseListener<T>) : Callback<T> {
    private var listener : ResponseListener<T> = mListener

    override fun onResponse(call: Call<T>, response: Response<T>) {
        val code = response.code()
        val body = response.body()
        val errorBody = response.errorBody()

        when(code) {
            200 -> {
                body?.let { listener.successResponse(it) }
            }

        }
    }
    interface ResponseListener<T> {
        fun successResponse(response: T)
        fun errorResponse(response: ResponseBody?)
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        if (t is java.lang.Exception) {
        } else {
            //do something else
        }
    }
}

