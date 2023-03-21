package com.oguzhancetin.pomodoro.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

class CategoryWithTask (
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val taskList: List<TaskItemEntity>
)