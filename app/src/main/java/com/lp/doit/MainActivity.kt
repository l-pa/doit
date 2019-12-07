package com.lp.doit

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.lp.doit.adapters.TodoAdapter
import com.lp.doit.data.Todo


class MainActivity : AppCompatActivity(),
    ActionBottomDialogFragment.ItemClickListener, TodoAdapter.TodoListener {

    lateinit var todos : ArrayList<Todo>

    lateinit var bar: BottomAppBar
    lateinit var fab: FloatingActionButton
    lateinit var todosFile: TodosFile
    lateinit var gson: Gson

    lateinit var nothingText: TextView
    lateinit var nothingImage: ImageView

    lateinit var todosList: RecyclerView

    lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nothingText = findViewById(R.id.nothingText)
        nothingImage = findViewById(R.id.nothingImage)
        todosList = findViewById(R.id.todosList)

        gson = Gson()
        todosFile = TodosFile("todo.json", this)
        todos = todosFile.loadTodos()
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        bar = findViewById<BottomAppBar>(R.id.bar)
        fab = findViewById<FloatingActionButton>(R.id.fab)
        bar.setOnClickListener(View.OnClickListener {
            showBottomSheet(it)
        })

        if (todos.size > 0) {
            nothingImage.visibility = View.GONE
            nothingText.visibility = View.GONE
        }

        adapter = TodoAdapter(this, todos)
        todosList.adapter = adapter

        adapter.notifyDataSetChanged()

        fab.setOnClickListener(View.OnClickListener {
            val i: Intent = Intent(this, AddTodoActivity::class.java)
            var tags = ArrayList<String>()
            for (todo in todos){
                if (todo.tags != null){
                    for (tag in todo.tags) {
                        tags.add(tag)
                    }
                }
            }
            i.putExtra("tags", tags)
            startActivityForResult(i, 1);
        })

    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    todosFile.addTodo(gson.fromJson(data.getStringExtra("todo"), Todo::class.java))
                    todos.clear()
                    todos.addAll(todosFile.loadTodos())
                    nothingImage.visibility = View.GONE
                    nothingText.visibility = View.GONE
                }
            }
        }
    }

    fun showBottomSheet(view: View?) {
        val addPhotoBottomDialogFragment =
            ActionBottomDialogFragment.newInstance()
        addPhotoBottomDialogFragment.show(
            supportFragmentManager,
            ActionBottomDialogFragment.TAG
        )
    }

    override fun onItemClick(item: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemRemove(id: Int, todo: Todo) {
        todos.removeAt(id)
        adapter.notifyDataSetChanged()
        todosFile.removeTodo(todo)
        if (todos.size == 0) {
            nothingText.visibility = View.VISIBLE
            nothingImage.visibility = View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()

    }
}
