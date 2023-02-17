package com.oguzhancetin.pomodoro.presentation.screen.main

import com.oguzhancetin.pomodoro.data.local.dao.TaskItemDao
import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity
import javax.inject.Inject

class MainRepository @Inject constructor(private val taskItemDao: TaskItemDao) {
    fun getFavoriteTaskItems() = taskItemDao.favoriteTaskItems()
    suspend fun updateTask(taskItem: TaskItemEntity) = taskItemDao.updateTaskItem(taskItem)


}