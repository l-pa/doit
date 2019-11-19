package com.lp.doit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddTodoActivity : AppCompatActivity() {

    private lateinit var todoTitle: EditText
    private lateinit var todoText: EditText
    private lateinit var todoDate: EditText
    private lateinit var todoTime: EditText
    private lateinit var todoSave: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        todoTitle = findViewById(R.id.todoTitle)
        todoText = findViewById(R.id.todoText)
        todoDate = findViewById(R.id.todoDate)
        todoTime = findViewById(R.id.todoTime)
        todoSave = findViewById(R.id.todoSave)


        todoSave.setOnClickListener {
            Toast.makeText(this, "Todo", Toast.LENGTH_LONG)
        }
    }
}
