package com.oguzhancetin.pomodoro.screen.task

import com.oguzhancetin.pomodoro.data.local.TaskItemDao
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import javax.inject.Inject

class TaskItemRepository @Inject constructor(private val taskItemDao: TaskItemDao) {
    fun getTaskItems() = taskItemDao.tasKItems()
    fun getFavoriteTaskItems() = taskItemDao.favoriteTaskItems()
    suspend fun deleteTaskItem(taskItem: TaskItem) = taskItemDao.delete(taskItem)
    suspend fun insertTaskItem(taskItem: TaskItem) = taskItemDao.insert(taskItem)
    fun getTaskItemById(id: Int) = taskItemDao.taskItem(id)
    suspend fun updateTask(taskItem: TaskItem) = taskItemDao.updateTaskItem(taskItem)
}