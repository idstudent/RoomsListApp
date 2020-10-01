package com.example.hotellistapp.view.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hotellistapp.R
import com.example.hotellistapp.databinding.ActivityRoomsDetailActivityBinding
import com.example.hotellistapp.model.ProductInfos
import com.example.hotellistapp.util.setOnSingleClickListener
import com.example.hotellistapp.viewModel.RoomsViewModel
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_rooms_detail_activity.*

class RoomsDetailActivity : AppCompatActivity() {
    private lateinit var item : ProductInfos
    private val viewModel : RoomsViewModel by viewModels()
    private lateinit var binding : ActivityRoomsDetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_rooms_detail_activity)
    }

    override fun onResume() {
        super.onResume()
        init()
    }
    private fun init() {
        if(intent.hasExtra("item")) {
            item = intent.getSerializableExtra("item") as ProductInfos
            binding.detailInfo = item
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
@BindingAdapter("detailImageUrl")
fun loadImage(imageView : ImageView, url : String) {
    Glide.with(imageView.context).load(url).into(imageView)
}