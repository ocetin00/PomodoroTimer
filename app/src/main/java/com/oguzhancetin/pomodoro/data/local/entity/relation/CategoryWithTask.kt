package com.oguzhancetin.pomodoro.data.local.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.oguzhancetin.pomodoro.data.local.entity.CategoryEntity
import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity

class CategoryWithTask (
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val taskList: List<TaskItemEntity>
)