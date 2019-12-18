package com.lp.doit.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.lp.doit.AddTodoActivity
import com.lp.doit.R
import com.lp.doit.TodoOverview
import com.lp.doit.data.Todo
import java.util.*
import kotlin.collections.ArrayList

class TodoAdapter (val removeEvent: TodoListener, val items: ArrayList<Todo>): RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    interface TodoListener {
        fun onItemRemove(id: Int, todo: Todo)
        fun onItemClick(id: Int, todo: Todo)

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val todoText = view.findViewById<TextView>(R.id.titleText)
        val todoDescription = view.findViewById<TextView>(R.id.descriptionText)
        val checkbox = view.findViewById<CheckBox>(R.id.checkBox)
        val layout = view.findViewById<ConstraintLayout>(R.id.todoConstraintLayout)
        val completeDay = view.findViewById<TextView>(R.id.completeDate)
        val colorView = view.findViewById<ImageView>(R.id.imageView)


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
        holder.colorView.setColorFilter(items[position].color)
        holder.todoDescription.text = items[position].description
        holder.completeDay.text = String.format("%02d.%02d. %04d ", items[position].completeDate?.get(Calendar.DAY_OF_MONTH),  items[position].completeDate?.get(
            Calendar.MONTH) as Int + 1,  items[position].completeDate?.get(Calendar.YEAR))
        holder.checkbox.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                Log.i("todo", items[position].name)
                removeEvent.onItemRemove(position, items[position])
                holder.checkbox.isChecked = false
            }
        }

        holder.layout.setOnClickListener{
            removeEvent.onItemClick(position, items[position])
        }
    }
}
