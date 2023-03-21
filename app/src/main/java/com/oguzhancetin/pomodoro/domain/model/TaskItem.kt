package com.oguzhancetin.pomodoro.domain.model

import java.util.*

/**
 * Created by ocetin00 on 15.01.2023
 */

data class TaskItem(
    val id: UUID,
    val description: String?,
    override val doneDate: Long?,
    val isFavorite: Boolean = false,
    val isFinished: Boolean = false,
    val createdDate: Long?,
    val categoryId:UUID? = null
) : Doneable
