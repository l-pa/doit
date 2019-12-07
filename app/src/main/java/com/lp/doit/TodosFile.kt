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
        val listType: Type = object : TypeToken<ArrayList<Todo>?>() {}.type
        val gson = Gson()
        Log.i("file", " F -> " + loadFromFile())
        val target2: ArrayList<Todo> = gson.fromJson(loadFromFile(), listType)
        Log.i("arraylist", target2.size.toString())
        return target2
    }

    fun addTodo(todo: Todo) {
        val listType = object : TypeToken<ArrayList<Todo>?>() {}.type
        val target: MutableList<Todo> = loadTodos()
        target.add(todo)
        val gson = Gson()
        val json = gson.toJson(target, listType)
        saveToFile(json)
    }
}