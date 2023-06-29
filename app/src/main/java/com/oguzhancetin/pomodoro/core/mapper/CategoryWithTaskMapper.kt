package com.oguzhancetin.pomodoro.core.mapper

import com.oguzhancetin.pomodoro.core.database.entity.relation.CategoryWithTask


fun CategoryWithTask.toCategoryWithTask(): CategoryWithTask {
    return CategoryWithTask(
        category = this.category,
        taskList = this.taskList
    )
}