package com.example.hotellistapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var type : String
) : RecyclerView.Adapter<RoomsListAdapter.ItemViewHolder>() {

    private lateinit var rememberListener : ItemClickListener
    private var items : ArrayList<ProductInfos> = ArrayList()
    private var rememberItems : ArrayList<ProductInfos> = ArrayList()

    fun updateItems(items : ArrayList<ProductInfos>) {
        this.items = items

        notifyDataSetChanged()
    }
    fun rememberItems(rememberItems : ArrayList<ProductInfos>) {
        this.rememberItems = rememberItems

        notifyDataSetChanged()
    }
    fun rememberListener(listener : ItemClickListener) {
        this.rememberListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_rooms, parent,false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind()
    }
    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val position = adapterPosition
            val item = items[position]

            itemView.title.text = item.name
            itemView.rank_text.text = item.rate.toString()
            Glide.with(context).load(item.thumbnail).into(itemView.thumbnail)

            if(type =="list") {
                if (rememberItems.isNotEmpty()) {
                    for (i in items.indices) {
                        for (j in rememberItems.indices) {
                            if (items[i].id == rememberItems[j].id) {
                                items[i].time = rememberItems[j].time
                                items[i].check = true
                                break
                            } else {
                                items[i].check = false
                            }
                        }
                    }
                } else {
                    for (i in items.indices) {
                        items[i].check = false
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