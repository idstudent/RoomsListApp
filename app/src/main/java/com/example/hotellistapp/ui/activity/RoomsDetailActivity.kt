package com.example.hotellistapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.hotellistapp.R
import com.example.hotellistapp.model.ProductInfos
import com.example.hotellistapp.util.setOnSingleClickListener
import kotlinx.android.synthetic.main.activity_rooms_detail_activity.*

class RoomsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms_detail_activity)

        init()
    }
    private fun init() {
        var check = false
        if(intent.hasExtra("item")) {
            val item = intent.getSerializableExtra("item") as ProductInfos
            title_text.text = item.name
            Glide.with(applicationContext).load(item.imgUrl).into(roomsImg)
            explain_text.text = item.subject
            price.text = "${item.price}원"
            rank_text.text = item.rate.toString()
            check = item.check
        }
        btn_remember_off.setOnSingleClickListener {
            btn_remember_off.visibility = View.GONE
            btn_remember_on.visibility = View.VISIBLE
            check = true
        }
        btn_remember_on.setOnSingleClickListener {
            btn_remember_off.visibility = View.VISIBLE
            btn_remember_on.visibility = View.GONE
            check = false
        }
    }
}