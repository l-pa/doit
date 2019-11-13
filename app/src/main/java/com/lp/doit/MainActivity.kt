package com.lp.doit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addTodoFab: View = findViewById(R.id.addTodoFab)

        addTodoFab.setOnClickListener { view ->
            Snackbar.make(view, "Add todo", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
    }
}
