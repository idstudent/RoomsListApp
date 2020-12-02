package com.example.hotellistapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.hotellistapp.R
import com.example.hotellistapp.db.entity.RoomsRememberEntity
import com.example.hotellistapp.model.ProductInfos
import com.example.hotellistapp.model.TProductInfos
import com.example.hotellistapp.util.setOnSingleClickListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_rooms_detail_activity.*
import java.text.SimpleDateFormat
import java.util.*

class RoomsDetailActivity : BaseActivity() {
    private lateinit var item : TProductInfos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms_detail_activity)
    }

    override fun onResume() {
        super.onResume()
        init()
        likeCheck()
    }
    private fun init() {
        main_tablayout.visibility = View.GONE

        var id = -1

        if(intent.hasExtra("item")) {
            item = intent.getSerializableExtra("item") as TProductInfos

            id = item.id
            title_text.text = item.name
            Glide.with(applicationContext).load(item.descriptionList.imagePath).into(roomsImg)
            explain_text.text = item.descriptionList.subject
            price.text = "${item.descriptionList.price}원"
            rank_text.text = item.rate.toString()
        }
        btn_remember_off.setOnSingleClickListener {
            btn_remember_off.visibility = View.GONE
            btn_remember_on.visibility = View.VISIBLE
            insertList(item)
        }
        btn_remember_on.setOnSingleClickListener {
            btn_remember_off.visibility = View.VISIBLE
            btn_remember_on.visibility = View.GONE
            deleteList(id)
        }
    }
    private fun likeCheck() {
        compositeDisposable.add(
            dbManager.roomsRememberDAO().select()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    for(i in it.indices) {
                        if (item.id == it[i].id){
                            btn_remember_off.visibility = View.GONE
                            btn_remember_on.visibility = View.VISIBLE
                        }
                    }
                }, {

                })
        )
    }
    private fun insertList(item : TProductInfos) {
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var curTime = dateFormat.format(Date(time))
        val insert = Observable.just(
            RoomsRememberEntity(
                id = item.id,
                name = item.name,
                thumbnail = item.thumbnail,
                imgPath = item.descriptionList.imagePath,
                subject = item.descriptionList.subject,
                price = item.descriptionList.price,
                rate = item.rate,
                time = curTime,
                check = true
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnNext {
                dbManager.roomsRememberDAO().insert(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                Toast.makeText(this@RoomsDetailActivity,R.string.insert_text,Toast.LENGTH_SHORT).show()
            }
        compositeDisposable.add(insert)
    }
    private fun deleteList(id : Int) {
        compositeDisposable.add(
            Observable.fromCallable {
                dbManager.roomsRememberDAO().deleteItem(id)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(this@RoomsDetailActivity,R.string.delete_text, Toast.LENGTH_SHORT).show()
                },{
                    Log.e("tag", "exception $it")
                })
        )
    }
}