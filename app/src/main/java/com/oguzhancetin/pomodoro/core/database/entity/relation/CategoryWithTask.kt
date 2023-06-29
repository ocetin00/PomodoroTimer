package com.oguzhancetin.pomodoro.core.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.oguzhancetin.pomodoro.core.database.entity.CategoryEntity
import com.oguzhancetin.pomodoro.core.database.entity.TaskItemEntity

class CategoryWithTask (
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val taskList: List<TaskItemEntity>
)