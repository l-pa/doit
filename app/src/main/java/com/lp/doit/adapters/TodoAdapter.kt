package com.lp.doit.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lp.doit.R
import com.lp.doit.data.Notification
import com.lp.doit.data.Todo

class TodoAdapter (val removeEvent: TodoListener, val items: ArrayList<Todo>): RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    interface TodoListener {
        fun onItemRemove(id: Int, todo: Todo)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val todoText = view.findViewById<TextView>(R.id.titleText)
        val todoDescription = view.findViewById<TextView>(R.id.tagsText)
        val checkbox = view.findViewById<CheckBox>(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.todo_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("name", "name " + items[position].name)
        holder.todoText.text = items[position].name
        holder.todoDescription.text = items[position].description

        holder.checkbox.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                Log.i("todo", items[position].name)
                removeEvent.onItemRemove(position, items[position])
            }
        }
    }
}
