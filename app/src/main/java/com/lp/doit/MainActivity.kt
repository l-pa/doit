package com.lp.doit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lp.doit.data.Todo
import com.lp.doit.data.TodosArr
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Type


class MainActivity : AppCompatActivity(), ActionBottomDialogFragment.ItemClickListener {

    lateinit var todos : ArrayList<Todo>

   private fun loadTodos() {
        val fileName : String = "todos.json"

       val file = baseContext.getFileStreamPath(fileName)
       if (file.exists()) {
            val bufferedReader: BufferedReader = file.bufferedReader()
            val jsonString = bufferedReader.use { it.readText() }
         //   todos = Gson().fromJson(jsonString, TodosArr)
           todos = ArrayList()
            Log.i("loadJson", "Loaeded +${todos.size}")
        } else {
            Log.i("loadJson", "Not loaded")
            todos = ArrayList<Todo>()
            val fos: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            fos.write("{}".toByteArray())
            fos.close()
        }

       if (todos.size == 0) {
           findViewById<com.google.android.material.textview.MaterialTextView>(R.id.emptySheetBar)?.visibility = View.VISIBLE
       }

    }

    lateinit var bar: BottomAppBar
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        loadTodos()
        bar = findViewById<BottomAppBar>(R.id.bar)
        fab = findViewById<FloatingActionButton>(R.id.fab)
        bar.setOnClickListener(View.OnClickListener {
            showBottomSheet(it)
        })

        fab.setOnClickListener(View.OnClickListener {
            val i: Intent = Intent(this, AddTodoActivity::class.java)
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
