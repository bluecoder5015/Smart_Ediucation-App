package com.example.smart_education

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todos.view.*

class TodoAdapter(
        private var todo: List<Todos>
    ): RecyclerView.Adapter<TodoAdapter.Todoviewholder>() {

        inner class Todoviewholder(itemview : View) : RecyclerView.ViewHolder(itemview)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Todoviewholder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todos,parent,false)
            return Todoviewholder(view)
        }

        override fun getItemCount(): Int {
            return todo.size
        }

        override fun onBindViewHolder(holder: Todoviewholder, position: Int) {
            holder.itemView.apply {
                title_todo.text =todo[position].title
                time_todo.text = todo[position].time
                done_checkbox.isChecked =todo[position].ischecked
            }
        }

    }