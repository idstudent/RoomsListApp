package com.example.hotellistapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotellistapp.R
import com.example.hotellistapp.listener.ItemClickListener
import com.example.hotellistapp.model.ProductInfos
import com.example.hotellistapp.ui.activity.RoomsDetailActivity
import com.example.hotellistapp.util.setOnSingleClickListener
import kotlinx.android.synthetic.main.item_rooms.view.*

class RoomsListAdapter (
    private val context : Context,
    private val listItems : List<ProductInfos>,
    private var rememberList : List<ProductInfos>,
    private var type : String
) : RecyclerView.Adapter<RoomsListAdapter.ItemViewHolder>() {

    private lateinit var rememberListener : ItemClickListener

    fun rememberListener(listener : ItemClickListener) {
        this.rememberListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_rooms, parent,false)
        )
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind()
    }
    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val position = adapterPosition
            val item = listItems[position]

            itemView.title.text = item.name
            itemView.rank_text.text = item.rate.toString()
            Glide.with(context).load(item.thumbnail).into(itemView.thumbnail)

            if(type =="list") {
                if (rememberList.isNotEmpty()) {
                    itemView.add_time_title.visibility = View.VISIBLE
                    itemView.add_time_text.visibility = View.VISIBLE

                    for (i in listItems.indices) {
                        for (j in rememberList.indices) {
                            if (listItems[i].id == rememberList[j].id) {
                                listItems[i].time = rememberList[j].time
                                listItems[i].check = true
                            } else {
                                listItems[i].check = false
                            }
                        }
                    }
                } else {
                    for (i in listItems.indices) {
                        listItems[i].check = false
                    }
                }
            }
            if(type == "remember") {
                itemView.add_time_title.visibility = View.VISIBLE
                itemView.add_time_text.visibility = View.VISIBLE
                itemView.add_time_text.text = item.time
            }else {
                itemView.add_time_title.visibility = View.GONE
                itemView.add_time_text.visibility = View.GONE
            }

            if(item.check){
                rememberBtnOn(itemView)
            }else {
                rememberBtnOff(itemView)
            }
            itemView.setOnSingleClickListener {
                val intent = Intent(context, RoomsDetailActivity::class.java)
                intent.putExtra("item", item)
                context.startActivity(intent)
            }
            itemView.btn_remember_off.setOnSingleClickListener {
                item.check = true
                rememberBtnOn(itemView)

                rememberListener.onClick(item)
            }
            itemView.btn_remember_on.setOnSingleClickListener {
                item.check = false

                rememberBtnOff(itemView)
                rememberListener.onClick(item)
            }
        }
    }
    private fun rememberBtnOn(itemView: View) {
        itemView.btn_remember_off.visibility = View.GONE
        itemView.btn_remember_on.visibility = View.VISIBLE
    }
    private fun rememberBtnOff(itemView: View) {
        itemView.btn_remember_off.visibility = View.VISIBLE
        itemView.btn_remember_on.visibility = View.GONE
    }

}