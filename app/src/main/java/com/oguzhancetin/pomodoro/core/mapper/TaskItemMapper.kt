package com.oguzhancetin.pomodoro.core.mapper

import com.oguzhancetin.pomodoro.core.database.entity.TaskItemEntity
import com.oguzhancetin.pomodoro.core.model.TaskItem
import java.util.UUID

/**
 * Created by ocetin00 on 15.01.2023
 */

fun TaskItemEntity.toMapTaskItem(): TaskItem {
    return TaskItem(
        id = 1,//this.id,
        createdDate = this.createdDate,
        description = this.description,
        doneDate = this.doneDate,
        isFavorite = this.isFavorite,
        isFinished = this.isFinished,
        categoryId = 1,//this.categoryId
    )
}


fun TaskItem.toMapTaskEntity(): TaskItemEntity {
    return TaskItemEntity(
        id = UUID.fromString(""),//this.id,
        createdDate = this.createdDate,
        description = this.description,
        doneDate = this.doneDate,
        isFavorite = this.isFavorite,
        isFinished = this.isFinished,
        categoryId = UUID.fromString("")// this.categoryId
    )
}