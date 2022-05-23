package com.example.homeanimationpoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecycleViewItem(): RecyclerView.Adapter<RecycleViewItem.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card_list, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 13

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ItemViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {
        fun bind() {

        }
    }
}