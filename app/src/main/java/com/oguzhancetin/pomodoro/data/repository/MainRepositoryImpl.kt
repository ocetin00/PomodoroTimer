package com.oguzhancetin.pomodoro.data.repository

import com.oguzhancetin.pomodoro.core.database.dao.TaskItemDao
import com.oguzhancetin.pomodoro.core.database.entity.TaskItemEntity

class MainRepositoryImpl  constructor(private val taskItemDao: TaskItemDao) : MainRepository{
        override fun getFavoriteTaskItems() = taskItemDao.favoriteTaskItems()
        override suspend fun updateTask(taskItem: TaskItemEntity) = taskItemDao.updateTaskItem(taskItem)


}
