package com.lp.doit

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.lp.doit.adapters.TagAdapter
import com.lp.doit.adapters.TodoAdapter
import com.lp.doit.data.Todo
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(),
    ActionBottomDialogFragment.ItemClickListener, TodoAdapter.TodoListener, TagAdapter.ITag {

    lateinit var todos : ArrayList<Todo>

    lateinit var todosToday : ArrayList<Todo>
    lateinit var todosWeek : ArrayList<Todo>
    lateinit var todosLater : ArrayList<Todo>



    lateinit var bar: BottomAppBar
    lateinit var fab: FloatingActionButton
    lateinit var todosFile: TodosFile
    lateinit var gson: Gson

    lateinit var nothingText: TextView
    lateinit var nothingImage: ImageView

    lateinit var coordinatorView: View

    lateinit var todosList: RecyclerView
    lateinit var weekLisk: RecyclerView
    lateinit var laterList: RecyclerView

    lateinit var adapter: TodoAdapter
    lateinit var adapterWeek: TodoAdapter
    lateinit var adapterLater: TodoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i("main", "activity created")
        Notifications().createNotificationChannel(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager, "Notifications")

        nothingText = findViewById(R.id.nothingText)
        nothingImage = findViewById(R.id.nothingImage)
        todosList = findViewById(R.id.todosListToday)


        coordinatorView = findViewById(R.id.coordinatorLayoutMain)

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

        fab.setOnClickListener(View.OnClickListener {
            val i: Intent = Intent(this, AddTodoActivity::class.java)
            var tags = ArrayList<String>()
            for (todo in todosFile.loadTodos()){
                if (todo.tags != null){
                    for (tag in todo.tags) {
                        tags.add(tag)
                    }
                }
            }
            i.putExtra("tags", ArrayList<String>(tags.distinct()))
            startActivityForResult(i, 1);
        })


    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i("result", "result")
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    todosFile.addTodo(gson.fromJson(data.getStringExtra("todo"), Todo::class.java))
                    todos.clear()
                    todos.addAll(todosFile.loadTodos())
                    adapter.notifyDataSetChanged()
                    nothingImage.visibility = View.GONE
                    nothingText.visibility = View.GONE
                }
            }
        }
    }

    private fun showBottomSheet(view: View?) {
        val addPhotoBottomDialogFragment =
            ActionBottomDialogFragment.newInstance(this, todosFile.loadTodos())
        addPhotoBottomDialogFragment.show(
            supportFragmentManager,
            ActionBottomDialogFragment.TAG
        )

    }

    override fun onItemRemove(id: Int, todo: Todo) {
        todos.removeAt(id)
        adapter.notifyDataSetChanged()
        val removedTodo = todo
        todosFile.removeTodo(todo)

        if (todosFile.loadTodos().size == 0) { // TODO
            nothingText.visibility = View.VISIBLE
            nothingImage.visibility = View.VISIBLE
        }

        Snackbar.make(coordinatorView, "Todo was deleted", Snackbar.LENGTH_LONG)
            .setAction("Restore todo", View.OnClickListener {
                todosFile.addTodo(removedTodo)
                var list = ArrayList<Todo>()
                list.clear()
                list.addAll(todosFile.loadTodos())
                todos.clear()
                todos.addAll(list)
                adapter.notifyDataSetChanged()

                if (todos.size > 0) {
                    nothingImage.visibility = View.GONE
                    nothingText.visibility = View.GONE
                }
            })
            .show();
    }

    override fun onItemClick(id: Int, todo: Todo) {
        val i = Intent(this, AddTodoActivity::class.java)
        i.putExtra("id", todo.id)
        var tags = ArrayList<String>()
        for (todo in todosFile.loadTodos()){
            if (todo.tags != null){
                for (tag in todo.tags) {
                    tags.add(tag)
                }
            }
        }
        i.putExtra("tags", ArrayList<String>(tags.distinct()))
        startActivityForResult(i, 1)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onItemClick(item: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun tagClick(tag: String) {
        var list = ArrayList<Todo>()
        list.clear()
        list.addAll(todosFile.loadTodos().filter { todo -> todo.tags.contains(tag) })
        todos.clear()
        todos.addAll(list)
        adapter.notifyDataSetChanged()
    }

    override fun tagClick(tag: Int) {
        if (tag == 0) {
            todos.clear()
            todos.addAll(todosFile.loadTodos())
            adapter.notifyDataSetChanged()
        }
    }
}
