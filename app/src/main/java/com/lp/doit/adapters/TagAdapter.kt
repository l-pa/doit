package com.lp.doit.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lp.doit.MainActivity
import com.lp.doit.R
import com.lp.doit.data.Attachment
import com.lp.doit.data.Tag


class TagAdapter (val i : ITag, val items: ArrayList<Tag>): RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    interface ITag {
        fun tagClick(tag : String)
        fun tagClick(tag : Int)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val tagName = view.findViewById<Button>(R.id.tagText)
        val tagCount = view.findViewById<Button>(R.id.tagCount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.tag_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tagName.text = items[position].name
        holder.tagCount.text = items[position].count.toString()
        holder.tagName.setOnClickListener {
            i.tagClick(items[position].name)
        }
    }
}
