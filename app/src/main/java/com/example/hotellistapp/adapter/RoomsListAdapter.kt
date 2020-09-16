package com.example.hotellistapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotellistapp.R
import com.example.hotellistapp.model.ProductInfos
import kotlinx.android.synthetic.main.item_rooms.view.*

class RoomsListAdapter (
    private val context : Context,
    private val listItems : List<ProductInfos>
    ) : RecyclerView.Adapter<RoomsListAdapter.ItemViewHolder>() {

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
            }
        }
    }