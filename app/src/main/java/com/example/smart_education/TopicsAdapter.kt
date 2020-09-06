package com.example.smart_education

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.topics.view.*

class TopicsAdapter(
    private var todo: List<Topics>
    ): RecyclerView.Adapter<TopicsAdapter.Todoviewholder>() {

        inner class Todoviewholder(itemview : View) : RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Todoviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.topics, parent, false)
        return Todoviewholder(view)
    }

        override fun getItemCount(): Int {
            return todo.size
        }

        override fun onBindViewHolder(holder: Todoviewholder, position: Int) {
            holder.itemView.apply {
                title_topic.text =todo[position].topic
            }
        }

}