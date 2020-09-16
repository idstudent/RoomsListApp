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
    private val listItems : List<ProductInfos>
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

            itemView.setOnSingleClickListener {
                val intent = Intent(context, RoomsDetailActivity::class.java)
                intent.putExtra("item", item)
                context.startActivity(intent)
            }
            itemView.btn_remember_off.setOnSingleClickListener {
                item.check = true
                itemView.btn_remember_off.visibility = View.GONE
                itemView.btn_remember_on.visibility = View.VISIBLE

                rememberListener.onClick(item)
            }
            itemView.btn_remember_on.setOnSingleClickListener {
                item.check = false
                itemView.btn_remember_off.visibility = View.VISIBLE
                itemView.btn_remember_on.visibility = View.GONE

                rememberListener.onClick(item)
            }
        }
    }
}