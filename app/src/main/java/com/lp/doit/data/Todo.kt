package com.lp.doit.data

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

data class Todo(
    val name: String,
    val description: String?,
    val dateCreate: Date,
    val subTasks: Array<String>?,
    val position: String?,
    val completeDate: Calendar?,
    val tags: Array<String>?,
    val reminders: ArrayList<Notification>?,
    val attachment: File?,
    val color: String?
)