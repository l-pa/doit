package com.lp.doit

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Status
import com.google.android.gms.common.internal.Constants
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.lp.doit.adapters.AttachmentAdapter
import com.lp.doit.adapters.NotificationAdapter
import com.lp.doit.adapters.RecyclerEvents
import com.lp.doit.data.Attachment
import com.lp.doit.data.Notification
import com.lp.doit.data.Todo
import dev.sasikanth.colorsheet.ColorSheet
import java.io.File
import java.util.*
import java.util.Arrays.asList
import kotlin.collections.ArrayList


class AddTodoActivity : AppCompatActivity(), TimeDialog.TimePickerListener, DateDialog.DatePickerListener, RecyclerEvents {

    private lateinit var scrollView: ScrollView

    private lateinit var cancelButton: Button
    private lateinit var addButton: Button
    private lateinit var addNotificationButton: Button
    private lateinit var addColorButton: Button
    private lateinit var addTagButton: Button
    private lateinit var attachmentButton: Button

    private lateinit var todoLayout: com.google.android.material.textfield.TextInputLayout
    private lateinit var descriptionLayout: com.google.android.material.textfield.TextInputLayout


    private var timeTodo : Calendar = Calendar.getInstance().clone() as Calendar

    lateinit var geofencingClient: GeofencingClient

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

    private lateinit var allTagsFromIntent : ArrayList<String>

    private lateinit var checkedTags : ArrayList<Boolean>

    private lateinit var nPosition : Place


    private var color = ColorSheet.NO_COLOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        allTagsFromIntent = intent.getStringArrayListExtra("tags")
        checkedTags = ArrayList()

        geofencingClient = LocationServices.getGeofencingClient(this)

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyA9xVxa94Iw7m5fq-foAekfJ8agiV_f07M", Locale.ENGLISH);
        }
        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

        // Specify the types of place data to return.
        autocompleteFragment!!.setPlaceFields(asList(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {

            override fun onPlaceSelected(place: Place) { // TODO: Get info about the selected place.
                nPosition = place
            }

            override fun onError(status: Status) { // TODO: Handle the error.
                Log.i("postion", "An error occurred: $status")
            }
        })

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


        for (i in 0 until allTagsFromIntent.size step 1) {
            tagsArr.add(allTagsFromIntent[i])
            checkedTags.add(false)
        }


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

            val tagsToAdd = ArrayList<String>()

            for (i in 0 until tagsArr.size) {
                if (checkedTags[i]) {
                    tagsToAdd.add(tagsArr[i])
                }
            }



            var broadcastID = 0

            for (alert in notificationArr) {
                Log.i("alert", alert.timeBefore.toString())
                var alarmMgr: AlarmManager?
                var alarmIntent: PendingIntent
                var notificationIntent = Intent(this, Notifications::class.java)
                notificationIntent.putExtra("name", todoTitle.text.toString())
                notificationIntent.putExtra("description", todoText.text.toString())

                alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmIntent = notificationIntent.let { intent ->

                    PendingIntent.getBroadcast(this, broadcastID, intent, 0)
                }
                broadcastID++
                when(alert.timeUnit) {
                    "minutes" -> {
                        Log.i("notification", alert.timeBefore.toString())
                        var notificationTime = timeTodo.clone() as Calendar
                        notificationTime.add(Calendar.MINUTE, (-alert.timeBefore.toInt()))
                        alarmMgr?.set(
                            AlarmManager.RTC_WAKEUP,
                            notificationTime.timeInMillis,
                            alarmIntent
                        )
                    }
                    "hours" -> {
                        var notificationTime = timeTodo.clone() as Calendar
                        notificationTime.add(Calendar.HOUR_OF_DAY, -alert.timeBefore.toInt())
                        alarmMgr?.set(
                            AlarmManager.RTC_WAKEUP,
                            notificationTime.timeInMillis,
                            alarmIntent
                        )
                    }

                    "days" -> {
                        var notificationTime = timeTodo.clone() as Calendar
                        notificationTime.add(Calendar.DAY_OF_MONTH, -alert.timeBefore.toInt())
                        alarmMgr?.set(
                            AlarmManager.RTC_WAKEUP,
                            notificationTime.timeInMillis,
                            alarmIntent
                        )
                    }
                }
            }
            if (::nPosition.isInitialized) {
                nPosition.latLng?.latitude?.let { it1 ->
                    nPosition.latLng?.longitude?.let { it2 ->
                        val g = Geofence.Builder()
                            // Set the request ID of the geofence. This is a string to identify this
                            // geofence.
                            .setRequestId(nPosition.name)
                            .setExpirationDuration(timeTodo.timeInMillis)
                            // Set the circular region of this geofence.
                            .setCircularRegion(
                                it1,
                                it2,
                                50f
                            )
                            // Set the transition types of interest. Alerts are only generated for these
                            // transition. We track entry and exit transitions in this sample.
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)

                            // Create the geofence.
                            .build()

                        val geofencingRequest = GeofencingRequest.Builder().apply {
                            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                            addGeofence(g)
                        }.build()

                        val geofencePendingIntent: PendingIntent by lazy {
                            val intent = Intent(this, GeofenceNotification::class.java)

                            intent.putExtra("city", nPosition.name)

                            PendingIntent.getBroadcast(this, ++broadcastID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                        }

                        val geofencingClient = LocationServices.getGeofencingClient(this)

                        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                            addOnFailureListener {
                                Toast.makeText(baseContext, "Geofence error", Toast.LENGTH_SHORT).show()
                            }
                            addOnSuccessListener {
                                Toast.makeText(baseContext, "Geofence success", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            val todo : Todo
            if (::nPosition.isInitialized) {
                todo = Todo(
                    todoTitle.text.toString(),
                    todoText.text.toString(),
                    Calendar.getInstance().time,
                    null,
                    nPosition.name,
                    nPosition.latLng?.longitude,
                    nPosition.latLng?.latitude,
                    timeTodo,
                    tagsToAdd,
                    notificationArr,
                    attachmentArr,
                    color
                )
            } else {
                todo = Todo(
                    todoTitle.text.toString(),
                    todoText.text.toString(),
                    Calendar.getInstance().time,
                    null,
                    null,
                    null,
                    null,
                    timeTodo,
                    tagsToAdd,
                    notificationArr,
                    attachmentArr,
                    color
                )
            }
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
                            notificationArr.add(Notification(15, "minutes"))
                            dialog.cancel()
                        }
                        1 -> {
                            notificationArr.add(Notification(1, "hours"))
                            dialog.cancel()
                        }
                            2 -> {
                                notificationArr.add(Notification(1, "days"))
                                dialog.cancel()
                            }

                                3 -> {
                                    val layoutInflater = LayoutInflater.from(this)
                                    val view: View = layoutInflater.inflate(R.layout.new_notification, null)
                                    MaterialAlertDialogBuilder(this)
                                        .setView(view)
                                        .setPositiveButton(
                                            "Add",
                                            DialogInterface.OnClickListener { dialog, which ->
                                                var selectedRadio: String = ""
                                                val index: Int =
                                                    view.findViewById<RadioGroup>(R.id.getNotificationRadioGroup).indexOfChild(
                                                        view.findViewById(view.findViewById<RadioGroup>(R.id.getNotificationRadioGroup).getCheckedRadioButtonId())
                                                    )

                                                when (index) {
                                                    0 -> selectedRadio = "minutes"
                                                    1 -> selectedRadio = "hours"
                                                    2 -> selectedRadio = "days"

                                                }
                                                notificationArr.add(
                                                    Notification(
                                                        view.findViewById<TextInputEditText>(
                                                            R.id.getNotificationTime
                                                        ).text.toString().toInt(), selectedRadio
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

            // TODO check selected tags

            var checked = BooleanArray(checkedTags.size)

            for (i in 0 until checkedTags.size step 1) {
                checked[i] = checkedTags[i]
            }

            allTagsFromIntent.toArray()

            var cs: Array<CharSequence> =
                tagsArr.toArray(arrayOfNulls<CharSequence>(tagsArr.size))
            MaterialAlertDialogBuilder(this)
                .setTitle("Tags")
                .setMultiChoiceItems(cs, checked, DialogInterface.OnMultiChoiceClickListener{
                    dialog, which, isChecked ->  checkedTags[which] = isChecked
                })
                .setPositiveButton("Ok", DialogInterface.OnClickListener{
                    dialog, which ->
                })
                .setNeutralButton("New", DialogInterface.OnClickListener{_dialog, _which ->
                    val layoutInflater = LayoutInflater.from(this)
                    val view: View = layoutInflater.inflate(R.layout.tags_selector, null)

                    MaterialAlertDialogBuilder(this)
                        .setView(view)
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                            dialog, which ->  dialog.dismiss()
                        })
                        .setPositiveButton("Add", DialogInterface.OnClickListener{dialog, which ->
                            if (!tagsArr.contains(view.findViewById<EditText>(R.id.newTagEditText).text.toString())) {
                            tagsArr.add(view.findViewById<EditText>(R.id.newTagEditText).text.toString())
                            checkedTags.add(true)
                            } else {
                                checkedTags[tagsArr.indexOf(view.findViewById<EditText>(R.id.newTagEditText).text.toString())] = true
                            }
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
        timeTodo.set(Calendar.HOUR_OF_DAY, hour)
        timeTodo.set(Calendar.MINUTE, minute)
        timeTodo.set(Calendar.SECOND, 0)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        todoDate.setText(String.format("%02d.%02d. %04d ", dayOfMonth, month + 1, year))
        dateSet = true
        timeTodo.set(Calendar.YEAR, year)
        timeTodo.set(Calendar.MONTH, month)
        timeTodo.set(Calendar.DAY_OF_MONTH, dayOfMonth)
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

    override fun deleteRecyclerItem(id: Int, item: Any) {
        when (item){
            is Attachment -> {
                attachmentArr.removeAt(id)
                attachmentAdapter.notifyDataSetChanged()
            }
            is Notification -> {
                notificationArr.removeAt(id)
                notificationAdapter.notifyDataSetChanged()
            }

        }
    }
}
