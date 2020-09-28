package com.example.hotellistapp.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.hotellistapp.R
import com.example.hotellistapp.model.ProductInfos
import com.example.hotellistapp.util.setOnSingleClickListener
import com.example.hotellistapp.viewModel.RoomsViewModel
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_rooms_detail_activity.*

class RoomsDetailActivity : BaseActivity() {
    private lateinit var item : ProductInfos
    private val viewModel : RoomsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms_detail_activity)
    }

    override fun onResume() {
        super.onResume()
        init()
    }
    private fun init() {
        main_tablayout.visibility = View.GONE

        if(intent.hasExtra("item")) {
            item = intent.getSerializableExtra("item") as ProductInfos

            title_text.text = item.name
            Glide.with(applicationContext).load(item.imgUrl).into(roomsImg)
            explain_text.text = item.subject
            price.text = "${item.price}원"
            rank_text.text = item.rate.toString()
        }
        btn_remember_off.setOnSingleClickListener {
            btn_remember_off.visibility = View.GONE
            btn_remember_on.visibility = View.VISIBLE
            viewModel.insert(item)
            Toast.makeText(this, R.string.insert_text, Toast.LENGTH_SHORT).show()
        }
        btn_remember_on.setOnSingleClickListener {
            btn_remember_off.visibility = View.VISIBLE
            btn_remember_on.visibility = View.GONE
            viewModel.delete(item)
            Toast.makeText(this, R.string.delete_text, Toast.LENGTH_SHORT).show()
        }
        likeCheck()
    }
    private fun likeCheck() {

        viewModel.apply {
            viewModel.detailActivityLikeCheck()
            viewModel.detailRememberItemLiveData.observe(this@RoomsDetailActivity, androidx.lifecycle.Observer {
                for(i in it.indices) {
                    if (item.id == it[i]){
                        btn_remember_off.visibility = View.GONE
                        btn_remember_on.visibility = View.VISIBLE
                    }
                }
            })
        }
    }
}