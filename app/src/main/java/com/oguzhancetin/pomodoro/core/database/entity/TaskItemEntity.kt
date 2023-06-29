package com.oguzhancetin.pomodoro.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "task_item")
data class TaskItemEntity(
    @PrimaryKey
    val id: UUID,
    val description: String? = null,
    @ColumnInfo(name = "create_date")
    val createdDate: Long? = null,
    @ColumnInfo(name = "done_date")
    var doneDate: Long? = null,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "is_finished")
    val isFinished: Boolean = false,
    var categoryId:UUID? = null
)
