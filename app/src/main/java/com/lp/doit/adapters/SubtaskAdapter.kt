package com.lp.doit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.lp.doit.R
import com.lp.doit.data.Notification
import com.lp.doit.data.Subtask

class SubtaskAdapter (val removeEvent: RecyclerEvents, val items: ArrayList<Subtask>): RecyclerView.Adapter<SubtaskAdapter.ViewHolder>() {

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val subtask = view.findViewById<Button>(R.id.subtast)
        val subtaskText = view.findViewById<Button>(R.id.subtaskText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent?.context).inflate(R.layout.time_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.subtaskText.text = items[position].todo

        holder.subtask.setOnClickListener {
            removeEvent.deleteRecyclerItem(position, items[position])
        }
    }
}
