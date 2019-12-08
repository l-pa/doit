package com.lp.doit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lp.doit.R
import com.lp.doit.data.Attachment


class TagAdapter (val removeEvent: RecyclerEvents, val items: ArrayList<String>): RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val tagText = view.findViewById<Button>(R.id.tagText)
        val tagDelete = view.findViewById<Button>(R.id.tagDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent?.context).inflate(R.layout.tag_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tagText.text = items[position]
        holder.tagDelete.setOnClickListener {
            removeEvent.deleteRecyclerItem(position, items[position])
        }
    }
}
