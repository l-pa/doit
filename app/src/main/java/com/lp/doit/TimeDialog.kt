package com.lp.doit

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimeDialog : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    interface TimePickerListener {
        fun onTimeSet(view: TimePicker?, hour: Int, minute: Int)
    }

    lateinit var listener: TimePickerListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as TimePickerListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var calendar = Calendar.getInstance()

        return TimePickerDialog(
            activity,
            this,
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            DateFormat.is24HourFormat(context)
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener.onTimeSet(view, hourOfDay, minute)
    }
}