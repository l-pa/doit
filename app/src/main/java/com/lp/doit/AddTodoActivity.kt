package com.lp.doit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddTodoActivity : AppCompatActivity() {

    private lateinit var cancelButton: Button
    private lateinit var addButton: Button

    private lateinit var todoTitle: EditText
    private lateinit var todoText: EditText
    private lateinit var todoDate: EditText
    private lateinit var todoTime: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        todoTitle = findViewById(R.id.todoTitle)
        todoText = findViewById(R.id.todoText)
        todoDate = findViewById(R.id.todoDate)
        todoTime = findViewById(R.id.todoTime)

        todoTime.setOnClickListener {

        }

    }
}
