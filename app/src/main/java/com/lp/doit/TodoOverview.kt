package com.lp.doit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class TodoOverview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_overview)

        val id = intent.extras?.getString("id")

        val todoFile = TodosFile("todo.json", this)

    }
}
