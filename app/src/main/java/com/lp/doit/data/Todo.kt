package com.lp.doit.data

import java.util.*
import kotlin.collections.ArrayList

data class Todo(
    val id: String,
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