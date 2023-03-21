package com.oguzhancetin.pomodoro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "task_category")
data class CategoryEntity(
    @PrimaryKey
    val id: UUID,
    @ColumnInfo(name = "name")
    var name: String?
)
