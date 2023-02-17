package com.oguzhancetin.pomodoro.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by ocetin00 on 15.01.2023
 */

data class TaskItem(
    val id: UUID,
    val description: String?,
    val createdDate: Long,
    val doneDate: Long?,
    val isFavorite: Boolean = false,
    val isFinished: Boolean = false
)
