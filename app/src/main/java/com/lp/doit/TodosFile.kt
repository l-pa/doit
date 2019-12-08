package com.lp.doit

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lp.doit.data.Todo
import java.io.BufferedReader
import java.io.FileOutputStream
import java.lang.reflect.Type


class TodosFile (val fileName: String, val context : Context) {

    private val listType: Type = object : TypeToken<ArrayList<Todo>?>() {}.type
    private val gson = Gson()

    private fun loadFromFile() : String {
        val file = context.getFileStreamPath(fileName)
        if (file.exists()) {
            val bufferedReader: BufferedReader = file.bufferedReader()
            return bufferedReader.use { it.readText() }
        } else {
            Log.i("loadJson", "Not loaded")
            saveToFile("[]")
            return "[]"
        }
    }

    private fun saveToFile(json: String) {
        val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fos.write(json.toByteArray())
        fos.close()
    }

    fun loadTodos() : ArrayList<Todo> {
        Log.i("file", " F -> " + loadFromFile())
        val target2: ArrayList<Todo> = gson.fromJson(loadFromFile(), listType)
        Log.i("arraylist", target2.size.toString())
        return target2
    }

    fun addTodo(todo: Todo) {
        val target: MutableList<Todo> = loadTodos()
        target.add(todo)
        saveToFile(gson.toJson(target, listType))
    }

    fun removeTodo(todo: Todo) {
        val target: MutableList<Todo> = loadTodos()
        target.remove(todo)
        saveToFile(gson.toJson(target, listType))
    }
}