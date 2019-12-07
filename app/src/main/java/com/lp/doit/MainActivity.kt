package com.lp.doit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.lp.doit.data.Todo


class MainActivity : AppCompatActivity(),
    ActionBottomDialogFragment.ItemClickListener {

    lateinit var todos : ArrayList<Todo>

    lateinit var bar: BottomAppBar
    lateinit var fab: FloatingActionButton
    lateinit var todosFile: TodosFile
    lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Log.i("todo", data.getStringExtra("todo"))
                    var todo = gson.fromJson(data.getStringExtra("todo"), Todo::class.java)
                    todosFile.addTodo(gson.fromJson(data.getStringExtra("todo"), Todo::class.java))
                    todos = todosFile.loadTodos()
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
}
