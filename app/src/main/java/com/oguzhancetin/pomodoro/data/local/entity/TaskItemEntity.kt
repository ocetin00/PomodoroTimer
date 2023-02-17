package com.oguzhancetin.pomodoro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "task_item")
data class TaskItemEntity(
    @PrimaryKey
    val id: UUID,
    val description: String?,
    @ColumnInfo(name = "create_date")
    val createdDate: Long,
    @ColumnInfo(name = "done_date")
    val doneDate: Long?,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "is_finished")
    val isFinished: Boolean = false
)
