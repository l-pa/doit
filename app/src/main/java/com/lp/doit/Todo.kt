package com.lp.doit

import java.time.LocalDateTime

data class Todo(
    val title: String,
    val text: String,
    val dateCreate: LocalDateTime,
    val subTasks: Array<Todo>?,
    val completeDate: LocalDateTime
)