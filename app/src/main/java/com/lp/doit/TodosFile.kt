package com.lp.doit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lp.doit.data.Todo
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


class TodosFile (val fileName: String, val context : Context) {

    private val listType: Type = object : TypeToken<ArrayList<Todo>?>() {}.type
    private val gson = Gson()

    public fun loadFromFile() : String {
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
        val target2: ArrayList<Todo> = gson.fromJson(loadFromFile(), listType)

        return ArrayList(target2.sortedWith(compareBy { it.completeDate }))
    }

    fun loadTodos(todos: String) : ArrayList<Todo> {
        val target2: ArrayList<Todo> = gson.fromJson(todos, listType)
        return ArrayList(target2.sortedWith(compareBy { it.completeDate }))
    }

    fun import(json: String) {
        val file = context.getFileStreamPath(fileName)
        file.delete()
        loadFromFile()
        var t = loadTodos(json)

        for (todo in t) {
            addTodo(todo)
        }
    }

    fun addTodo(todo: Todo) {
        var target: MutableList<Todo> = loadTodos()

        if (target.filter { Todo -> Todo.id == todo.id }.size > 0) {
            target = target.filter { Todo -> Todo.id != todo.id }.toMutableList()
        }

        target.add(todo)
        saveToFile(gson.toJson(target, listType))
    }

    fun removeTodo(todo: Todo) {
        val target: MutableList<Todo> = loadTodos()
        target.remove(todo)


        var broadcastID = 0
        for (alert in todo.reminders!!) {
            broadcastID++
            Log.i("alert", alert.timeBefore.toString())
            var alarmMgr: AlarmManager?
            var alarmIntent: PendingIntent
            var notificationIntent = Intent(context, Notifications::class.java)

            alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmIntent = notificationIntent.let { intent ->

                PendingIntent.getBroadcast(context, broadcastID, intent, 0)
            }
            broadcastID++
            when (alert.timeUnit) {
                "minutes" -> {
                    Log.i("notification", alert.timeBefore.toString())
                    var notificationTime = todo.completeDate?.clone() as Calendar
                    notificationTime.add(Calendar.MINUTE, (-alert.timeBefore.toInt()))
                    alarmMgr.cancel(
                        alarmIntent
                    )
                }
                "hours" -> {
                    var notificationTime = alert.timeBefore as Calendar // clone
                    notificationTime.add(Calendar.HOUR_OF_DAY, -alert.timeBefore.toInt())
                    alarmMgr.cancel(
                        alarmIntent
                    )
                }

                "days" -> {
                    var notificationTime = alert.timeBefore as Calendar
                    notificationTime.add(Calendar.DAY_OF_MONTH, -alert.timeBefore.toInt())
                    alarmMgr.cancel(
                        alarmIntent
                    )
                }
            }
        }

        saveToFile(gson.toJson(target, listType))
    }

}