package com.lp.doit

import android.app.Activity
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.lp.doit.adapters.AttachmentAdapter
import com.lp.doit.adapters.NotificationAdapter
import com.lp.doit.data.Attachment
import com.lp.doit.data.Notification
import com.lp.doit.data.Todo
import dev.sasikanth.colorsheet.ColorSheet
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class AddTodoActivity : AppCompatActivity(), TimeDialog.TimePickerListener, DateDialog.DatePickerListener, NotificationAdapter.DatePickerListener, AttachmentAdapter.AttachmentPickerListener {

    private lateinit var scrollView: ScrollView


    private lateinit var cancelButton: Button
    private lateinit var addButton: Button
    private lateinit var addNotificationButton: Button
    private lateinit var addColorButton: Button
    private lateinit var addTagButton: Button
    private lateinit var attachmentButton: Button

    private lateinit var todoLayout: com.google.android.material.textfield.TextInputLayout
    private lateinit var descriptionLayout: com.google.android.material.textfield.TextInputLayout


    private var timeTodo : Calendar = Calendar.getInstance()

    private lateinit var todoTitle: EditText
    private lateinit var todoText: EditText
    private lateinit var todoDate: Button
    private lateinit var todoTime: Button

    private lateinit var notificationsList: RecyclerView
    private lateinit var attachmentList: RecyclerView

    private lateinit var notificationAdapter : NotificationAdapter
    private lateinit var attachmentAdapter : AttachmentAdapter

    private var timeSet : Boolean = false
    private var dateSet : Boolean = false
    private var notificationArr = ArrayList<Notification>();
    private var attachmentArr = ArrayList<Attachment>();
    private var tagsArr = ArrayList<String>();


    private var color = ColorSheet.NO_COLOR


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        scrollView = findViewById(R.id.scrollView3)
        todoTitle = findViewById(R.id.todoTitle)
        todoText = findViewById(R.id.todoText)
        todoDate = findViewById(R.id.todoDate)
        todoTime = findViewById(R.id.todoTime)

        todoLayout = findViewById(R.id.todoTitleLayout)
        descriptionLayout = findViewById(R.id.todoTextLayout)

        notificationsList = findViewById(R.id.notificationsList)
        attachmentList = findViewById(R.id.attachmentList)

        notificationsList.isNestedScrollingEnabled = false
        attachmentList.isNestedScrollingEnabled = false

        addNotificationButton = findViewById(R.id.addNotificationButton)
        addColorButton = findViewById(R.id.colorButton)
        addTagButton = findViewById(R.id.tagsButton)
        attachmentButton = findViewById(R.id.attachmentButton)

        addButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        addButton.setOnClickListener{
            if (todoTitle.text.toString().isNullOrEmpty() ) {
                scrollView.fullScroll(ScrollView.FOCUS_UP)
                todoLayout.error = "Required"
                return@setOnClickListener
            }

            if (!dateSet) {
                DateDialog().show(supportFragmentManager, "datePicker")
                return@setOnClickListener
            }

            if (!timeSet){
                TimeDialog().show(supportFragmentManager, "timePicker")
                return@setOnClickListener
            }

            val todo = Todo(todoTitle.text.toString(), todoText.text.toString(), Calendar.getInstance().time, null, null, timeTodo, tagsArr, notificationArr, attachmentArr, color)
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

        attachmentButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, 1)
        }

        notificationAdapter =
            NotificationAdapter(this, notificationArr)
        attachmentAdapter =
            AttachmentAdapter(this, attachmentArr)

        notificationsList.adapter = notificationAdapter
        attachmentList.adapter = attachmentAdapter

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
                        Snackbar.make(contextView, "Set todo time", Snackbar.LENGTH_LONG)
                            .setAction("Set time", View.OnClickListener {
                                TimeDialog().show(supportFragmentManager, "timePicker")
                            })
                            .show();
                    }
                    if (!dateSet) {
                        val contextView: View = findViewById(R.id.coordinatorLayout)
                        Snackbar.make(contextView, "Set todo date", Snackbar.LENGTH_LONG)
                            .setAction("Set time", View.OnClickListener {
                                DateDialog().show(supportFragmentManager, "datePicker")
                            })
                            .show();

                    }
                    notificationAdapter.notifyDataSetChanged()
                    dialog.cancel()
                }
                .show();
        }

        addColorButton.setOnClickListener {
          /*  MaterialAlertDialogBuilder(this)
                .setView(R.layout.color_selector)
                .setPositiveButton("Add", DialogInterface.OnClickListener{ dialog, which ->
                    dialog.cancel()
                })*/

            // R.color.md_300

            ColorSheet().colorPicker(
                colors =  intArrayOf(R.color.md_purple_A700, R.color.md_purple_A700, R.color.md_purple_A700, R.color.md_purple_A700, R.color.md_purple_A700, R.color.md_purple_A700, R.color.md_purple_A700,R.color.md_purple_A700 ,R.color.md_purple_A700, R.color.md_purple_A700),
                noColorOption = true,
                listener = { color ->
                    addColorButton.setBackgroundColor(color)
                    this.color = color
                })
                .show(supportFragmentManager)

        }

        addTagButton.setOnClickListener {
            val tags = intent.getStringArrayListExtra("tags")
            tags.toArray()
            val cs: Array<CharSequence> =
                tags.toArray(arrayOfNulls<CharSequence>(tags.size))
            MaterialAlertDialogBuilder(this)
                .setTitle("Tags")
                .setMultiChoiceItems(cs, null, null)
                .setPositiveButton("Add", DialogInterface.OnClickListener{
                    dialog, which ->
                })
                .setNeutralButton("New", DialogInterface.OnClickListener{_dialog, _which ->
                    MaterialAlertDialogBuilder(this)
                        .setView(R.id.newTagEditText)
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                            dialog, which ->  dialog.dismiss()
                        })
                        .setPositiveButton("Add", DialogInterface.OnClickListener{dialog, which ->  
                        })
                        .show()

                  })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                    dialog, which ->  dialog.cancel()
                })
                .show()
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
        notificationAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val cR: ContentResolver = this.getContentResolver()
                    attachmentArr.add(Attachment(data.data, File(data.data?.path).name,
                        data.data?.let { cR.getType(it) }))
                }
                attachmentAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onAttachmentRemove(id: Int) {
        attachmentArr.removeAt(id)
        attachmentAdapter.notifyDataSetChanged()
    }
}
