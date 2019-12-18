package com.lp.doit.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lp.doit.R
import com.lp.doit.data.Attachment
import java.io.File


class AttachmentAdapter (val removeEvent: RecyclerEvents, val items: ArrayList<Attachment>): RecyclerView.Adapter<AttachmentAdapter.ViewHolder>() {

    interface AttachmentPickerListener {
        fun onAttachmentRemove(id: Int)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val attachmentName = view.findViewById<Button>(R.id.attachmentText)
        val attachmentImage = view.findViewById<ImageView>(R.id.attachmentImage)
        val attachmentDeleteButton = view.findViewById<Button>(R.id.attachmentDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent?.context).inflate(R.layout.attachment_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.attachmentName.text = items[position].filename
        holder.attachmentDeleteButton.setOnClickListener {
            removeEvent.deleteRecyclerItem(position, items[position])
        }
        if (items[position].type != null) {
            if (items[position].type!!.startsWith("image")) {
                holder.attachmentImage.setImageURI(Uri.parse(items[position].pathUri))
            } else {
                holder.attachmentImage.visibility = View.GONE
            }
        }
        holder.attachmentImage.setOnClickListener {
            removeEvent.clickRecyclerItem(position, items[position])
        }
    }
}
