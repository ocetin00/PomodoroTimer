package com.oguzhancetin.pomodoro.screen.main

import com.oguzhancetin.pomodoro.data.local.TaskItemDao
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import javax.inject.Inject

class MainRepository @Inject constructor(private val taskItemDao: TaskItemDao) {
    fun getFavoriteTaskItems() = taskItemDao.favoriteTaskItems()
    suspend fun updateTask(taskItem: TaskItem) = taskItemDao.updateTaskItem(taskItem)


}