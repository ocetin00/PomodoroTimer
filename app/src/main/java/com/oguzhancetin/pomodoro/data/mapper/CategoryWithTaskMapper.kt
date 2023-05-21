package com.oguzhancetin.pomodoro.data.mapper

import com.oguzhancetin.pomodoro.data.local.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.data.local.entity.PomodoroEntity
import com.oguzhancetin.pomodoro.domain.model.Pomodoro


fun CategoryWithTask.toCategoryWithTask(): CategoryWithTask {
    return CategoryWithTask(
        category = this.category,
        taskList = this.taskList
    )
}