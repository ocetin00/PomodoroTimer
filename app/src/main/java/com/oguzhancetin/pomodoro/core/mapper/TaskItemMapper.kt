package com.oguzhancetin.pomodoro.core.mapper

import com.oguzhancetin.pomodoro.core.database.entity.TaskItemEntity
import com.oguzhancetin.pomodoro.core.model.TaskItem

/**
 * Created by ocetin00 on 15.01.2023
 */

fun TaskItemEntity.toMapTaskItem(): TaskItem {
    return TaskItem(
        id = this.id,
        createdDate = this.createdDate,
        description = this.description,
        doneDate = this.doneDate,
        isFavorite = this.isFavorite,
        isFinished = this.isFinished,
        categoryId = this.categoryId
    )
}
fun TaskItem.toMapTaskEntity(): TaskItemEntity {
    return TaskItemEntity(
        id = this.id,
        createdDate = this.createdDate,
        description = this.description,
        doneDate = this.doneDate,
        isFavorite = this.isFavorite,
        isFinished = this.isFinished,
        categoryId = this.categoryId
    )
}