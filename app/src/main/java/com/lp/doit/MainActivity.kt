package com.lp.doit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    lateinit var bar: BottomAppBar
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        bar = findViewById<BottomAppBar>(R.id.bar)
        fab = findViewById<FloatingActionButton>(R.id.fab)
        bar.setOnClickListener(View.OnClickListener {

        })

        fab.setOnClickListener(View.OnClickListener {
            val i: Intent = Intent(this, AddTodoActivity::class.java)
            startActivityForResult(i, 1);
        })

    }
}
