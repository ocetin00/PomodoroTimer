package com.oguzhancetin.pomodoro.data.mapper

import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity
import com.oguzhancetin.pomodoro.domain.model.TaskItem

/**
 * Created by ocetin00 on 15.01.2023
 */

fun TaskItemEntity.toMapTaskItem(): TaskItem{
    return TaskItem(
        id = this.id,
        createdDate = this.createdDate,
        description = this.description,
        doneDate = this.doneDate,
        isFavorite = this.isFavorite,
        isFinished = this.isFinished
    )
}
fun TaskItem.toMapTaskEntity(): TaskItemEntity{
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