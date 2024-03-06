package com.oguzhancetin.pomodoro.data.repository

import app.cash.sqldelight.coroutines.asFlow
import com.oguzhancetin.pomodoro.PomodoroDb
import com.oguzhancetin.pomodoro.core.mapper.toMapTaskItem
import com.oguzhancetin.pomodoro.core.model.Category
import com.oguzhancetin.pomodoro.core.model.TaskItem
import com.oguzhancetin.pomodoro.core.util.FilterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class TaskItemRepositorySqLiteImpl constructor(private val db: PomodoroDb) : TaskItemRepository {

    private val queries = db.taskItemEntityQueries
    override fun getTaskItems(): Flow<List<TaskItem>> {
        return queries.selectAll()
            .asFlow()
            .map { query ->
                query.executeAsList().map { taskItem ->
                    TaskItem(
                        id = taskItem.id,
                        description = taskItem.description,
                        doneDate = taskItem.done_date,
                        isFavorite = (taskItem.is_favorite.toInt() == 1),
                        isFinished = (taskItem.is_finished.toInt() == 1),
                        categoryId = taskItem.categoryId
                    )
                }
            }
    }

    override fun getFavoriteTaskItems(): Flow<List<TaskItem>> {
        return queries.selectFavorite(1).asFlow().map { query ->
            query.executeAsList().map {
                TaskItem(
                    id = it.id,
                    description = it.description,
                    doneDate = it.done_date,
                    isFavorite = (it.is_favorite.toInt() == 1),
                    isFinished = (it.is_finished.toInt() == 1),
                    categoryId = it.categoryId
                )
            }
        }

    }

    override suspend fun deleteTaskItem(taskItem: TaskItem) {
        queries.delete(taskItem.id)
    }

    override suspend fun insertTaskItem(taskItem: TaskItem) {
        queries.insertOrRelace(
            id = taskItem.id,
            description = taskItem.description,
            categoryId = taskItem.categoryId,
            is_finished = (if (taskItem.isFinished) 1 else 0),
            is_favorite = (if (taskItem.isFavorite) 1 else 0),
            create_date = taskItem.createdDate,
            done_date = taskItem.doneDate
        )
    }

    override suspend fun getTaskItemById(id: UUID): TaskItem {
        val result = queries.getById(1).executeAsOne()
        return TaskItem(
            id = result.id,
            description = result.description,
            doneDate = result.done_date,
            isFavorite = (result.is_favorite.toInt() == 1),
            isFinished = (result.is_finished.toInt() == 1),
            categoryId = result.categoryId
        )

    }

    override fun getDoneTaskItems(filterType: FilterType): List<TaskItem> {
        TODO("Not yet implemented")
    }

    override fun getCurrentWeekTaskList(currentWeekMilist: Long): Flow<List<TaskItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(taskItem: TaskItem) {
        queries.insertOrRelace(
            id = taskItem.id,
            description = taskItem.description,
            categoryId = taskItem.categoryId,
            is_finished = (if (taskItem.isFinished) 1 else 0),
            is_favorite = (if (taskItem.isFavorite) 1 else 0),
            create_date = taskItem.createdDate,
            done_date = taskItem.doneDate
        )
    }

    override suspend fun getTaskByCategoryName(categoryName: String): Map<Category, List<TaskItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskByCategoryId(id: UUID): Map<Category, List<TaskItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteByCategoryId(categoryId: UUID) {
        TODO("Not yet implemented")
    }
}