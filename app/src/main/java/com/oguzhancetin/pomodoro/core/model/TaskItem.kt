package com.oguzhancetin.pomodoro.core.model

/**
 * Created by ocetin00 on 15.01.2023
 */

data class TaskItem(
    val id: Long,
    val description: String?,
    override var doneDate: Long? = null,
    var isFavorite: Boolean = true,
    var isFinished: Boolean = false,
    val createdDate: Long? = null,
    var categoryId: Long? = 0L
) : Doneable
