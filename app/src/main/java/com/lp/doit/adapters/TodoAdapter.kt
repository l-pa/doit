package com.lp.doit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.lp.doit.R
import com.lp.doit.data.Notification

class TodoAdapter (val removeEvent: DatePickerListener, val items: ArrayList<Notification>): RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    interface DatePickerListener {
        fun onItemRemove(id: Int)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val notificationTime = view.findViewById<Button>(R.id.notificationText)
        val notificationDelete = view.findViewById<Button>(R.id.notificationDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent?.context).inflate(R.layout.time_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.notificationTime.text = items[position].timeBefore.toString() + " " + items[position].timeUnit + " before"

        holder.notificationDelete.setOnClickListener {
            removeEvent.onItemRemove(position)
        }
    }
}
