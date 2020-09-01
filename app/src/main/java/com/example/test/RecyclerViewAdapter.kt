package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_listitem.view.*

class RecyclerViewAdapter(
    private val exampleList: List<ExampleItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val textView_topic: TextView = itemView.textView_topic
        val textView_hour: TextView = itemView.textView_hour
        val textView_date: TextView = itemView.textView_date
        val textView_text: TextView = itemView.textView_text

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_listitem,
            parent, false
        )

        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentItem = exampleList[position]

        holder.textView_date.text = currentItem.date
        holder.textView_hour.text = currentItem.hour
        holder.textView_text.text = currentItem.text
        holder.textView_topic.text = currentItem.topic
    }

    override fun getItemCount() = exampleList.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}