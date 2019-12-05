package com.lp.doit

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class AddTodoActivity : AppCompatActivity(), TimeFragment.TimePickerListener {

    private lateinit var cancelButton: Button
    private lateinit var addButton: Button
    private lateinit var addNotificationButton: Button

    private lateinit var todoTitle: EditText
    private lateinit var todoText: EditText
    private lateinit var todoDate: Button
    private lateinit var todoTime: Button

    private lateinit var notificationsList: ListView

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    var listItems = ArrayList<String>()

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }




        todoTitle = findViewById(R.id.todoTitle)
        todoText = findViewById(R.id.todoText)
        todoDate = findViewById(R.id.todoDate)
        todoTime = findViewById(R.id.todoTime)

        notificationsList = findViewById(R.id.notificationsList)
        addNotificationButton = findViewById(R.id.addNotificationButton)

        todoTime.setOnClickListener {
            val timeFragment = TimeFragment()
            timeFragment.show(supportFragmentManager, "timePicker")
        }

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listItems
        )
        notificationsList.adapter = adapter

        addNotificationButton.setOnClickListener {
            adapter.add("xd")
        }



    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        todoTime.setText(String.format("%02d:%02d", hour, minute))
    }
}
