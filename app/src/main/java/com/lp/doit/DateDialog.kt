package com.lp.doit

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DateDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {
    interface DatePickerListener {
        fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int)
    }

    lateinit var listener: DatePickerListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as DatePickerListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var calendar = Calendar.getInstance()
        return DatePickerDialog(activity as Context, this, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener.onDateSet(view, year, month, dayOfMonth)
    }
}