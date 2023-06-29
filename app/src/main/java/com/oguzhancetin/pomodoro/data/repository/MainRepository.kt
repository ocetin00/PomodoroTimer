package com.oguzhancetin.pomodoro.data.repository

import com.oguzhancetin.pomodoro.core.database.dao.TaskItemDao
import com.oguzhancetin.pomodoro.core.database.entity.TaskItemEntity
import javax.inject.Inject

class MainRepository @Inject constructor(private val taskItemDao: TaskItemDao) {
    fun getFavoriteTaskItems() = taskItemDao.favoriteTaskItems()
    suspend fun updateTask(taskItem: TaskItemEntity) = taskItemDao.updateTaskItem(taskItem)


}