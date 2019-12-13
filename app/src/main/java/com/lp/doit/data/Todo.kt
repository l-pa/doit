package com.lp.doit.data

import com.google.android.libraries.places.api.model.Place
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

data class Todo(
    val name: String,
    val description: String?,
    val dateCreate: Date,
    val subTasks: Array<String>?,
    val positionName: String?,
    val positionLang: Double?,
    val positionLat: Double?,
    val completeDate: Calendar?,
    val tags: ArrayList<String>,
    val reminders: ArrayList<Notification>?,
    val attachment: ArrayList<Attachment>,
    val color: Int
)