package com.example.homeanimationpoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecycleViewAnimation(): RecyclerView.Adapter<RecycleViewAnimation.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0, 2 -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_header, parent, false))
            else -> ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 3

    override fun getItemViewType(position: Int): Int {
        return position
    }

    open class ViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {
        open fun bind() { }
    }

    class ItemViewHolder(
        itemView: View
    ): ViewHolder(itemView) {
        val list: RecyclerView by lazy { itemView.findViewById(R.id.listItem) }

        override fun bind() {
            list.adapter = RecycleViewItem()
            list.isNestedScrollingEnabled = false

        }
    }
}