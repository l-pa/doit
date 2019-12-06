package com.lp.doit

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.lp.doit.data.Notification
import com.lp.doit.data.Todo
import java.util.*
import kotlin.collections.ArrayList


class AddTodoActivity : AppCompatActivity(), TimeDialog.TimePickerListener, DateDialog.DatePickerListener, NotificationAdapter.DatePickerListener {

    private lateinit var cancelButton: Button
    private lateinit var addButton: Button
    private lateinit var addNotificationButton: Button
    private lateinit var addColorButton: Button
    private lateinit var addTagButton: Button

    private var timeTodo : Calendar = Calendar.getInstance()

    private lateinit var todoTitle: EditText
    private lateinit var todoText: EditText
    private lateinit var todoDate: Button
    private lateinit var todoTime: Button

    private lateinit var notificationsList: RecyclerView
    private lateinit var adapter : NotificationAdapter
    private var timeSet : Boolean = false
    private var dateSet : Boolean = false
    private var notificationArr = ArrayList<Notification>();

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
        addColorButton = findViewById(R.id.colorButton)
        addTagButton = findViewById(R.id.tagsButton)

        addButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        addButton.setOnClickListener{
            val todo = Todo(todoTitle.text.toString(), todoText.text.toString(), Calendar.getInstance().time, null, null, timeTodo, null, notificationArr, null, null)
            val gson = Gson()
            val returnIntent = Intent()
            returnIntent.putExtra("todo", gson.toJson(todo))
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        todoTime.setOnClickListener {
            TimeDialog().show(supportFragmentManager, "timePicker")
        }

        todoDate.setOnClickListener {
            DateDialog().show(supportFragmentManager, "datePicker")
        }

        adapter = NotificationAdapter( this,notificationArr)

        notificationsList.adapter = adapter

        addNotificationButton.setOnClickListener {
            val singleItems = arrayOf("15 minutes before", "1 hour before", "1 day before", "Custom")
            val checkedItem = 0
            MaterialAlertDialogBuilder(this)
                .setTitle("Notification")
                .setSingleChoiceItems(singleItems, checkedItem) {
                    dialog, which ->
                    when(which){
                        0 -> {
                            notificationArr.add(Notification(15, "min"))
                            dialog.cancel()
                        }
                        1 -> {
                            notificationArr.add(Notification(1, "hour"))
                            dialog.cancel()
                        }
                            2 -> {
                                notificationArr.add(Notification(1, "day"))
                                dialog.cancel()
                            }

                                3 -> {
                                    MaterialAlertDialogBuilder(this)
                                        .setView(R.layout.new_notification)
                                        .setPositiveButton(
                                            "Add",
                                            DialogInterface.OnClickListener { dialog, which ->
                                                var selectedRadio: String = ""
                                                val index: Int =
                                                    findViewById<RadioGroup>(R.id.getNotificationRadioGroup).indexOfChild(
                                                        findViewById(findViewById<RadioGroup>(R.id.getNotificationRadioGroup).getCheckedRadioButtonId())
                                                    )

                                                when (index) {
                                                    0 -> selectedRadio = "minutes"
                                                    1 -> selectedRadio = "hours"
                                                    2 -> selectedRadio = "days"

                                                }
                                                notificationArr.add(
                                                    Notification(
                                                        findViewById<TextInputEditText>(
                                                            R.id.getNotificationTime
                                                        ).text as Number, selectedRadio
                                                    )
                                                )

                                                dialog.cancel()
                                            })
                                        .setNegativeButton("Close", DialogInterface.OnClickListener{ dialog, which ->
                                            dialog.cancel()
                                        })

                                        .show()
                                }
                    }
                    if (!timeSet){
                        val contextView: View = findViewById(R.id.coordinatorLayout)
                        Snackbar.make(contextView, "Set todo time", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Set time", View.OnClickListener {
                                TimeDialog().show(supportFragmentManager, "timePicker")
                            })
                            .show();
                    }
                    if (!dateSet) {
                        val contextView: View = findViewById(R.id.coordinatorLayout)
                        Snackbar.make(contextView, "Set todo date", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Set time", View.OnClickListener {
                                DateDialog().show(supportFragmentManager, "datePicker")
                            })
                            .show();

                    }
                    adapter.notifyDataSetChanged()
                    dialog.cancel()
                }
                .show();
        }

        addColorButton.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setView(R.layout.color_selector)
                .setPositiveButton("Add", DialogInterface.OnClickListener{ dialog, which ->
                    dialog.cancel()
                })
        }

        addTagButton.setOnClickListener {

            MaterialAlertDialogBuilder(this)
                .setView(R.layout.tags_selector)
                .setPositiveButton("Add", DialogInterface.OnClickListener{
                    dialog, which ->
                })
        }
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        todoTime.setText(String.format("%02d:%02d", hour, minute))
        timeSet = true
        timeTodo.set(Calendar.MINUTE, minute)
        timeTodo.set(Calendar.HOUR, hour)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        todoDate.setText(String.format("%02d.%02d. %04d ", dayOfMonth, month + 1, year))
        dateSet = true
        timeTodo.set(Calendar.YEAR, year)
        timeTodo.set(Calendar.MONTH, month + 1)
        timeTodo.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    override fun onItemRemove(id: Int) {
        notificationArr.removeAt(id)
        adapter.notifyDataSetChanged()
    }
}
