package com.oguzhancetin.pomodoro.data.repository

import app.cash.sqldelight.coroutines.asFlow
import com.oguzhancetin.pomodoro.CategoryEntityQueries
import com.oguzhancetin.pomodoro.PomodoroDb
import com.oguzhancetin.pomodoro.core.database.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.core.model.Category
import com.oguzhancetin.pomodoro.core.model.CategoryTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class CategoryRepositorySqDelightImpl(private val categoryEntityQueries: CategoryEntityQueries) :
    CategoryRepository {
    override suspend fun upsertCategory(category: Category) {
        categoryEntityQueries.insertOrRelace(
            id = 1,
            name = category.name
        )
    }

    override suspend fun deleteCategory(category: Category) {
        categoryEntityQueries.delete(1)
    }

    override suspend fun deleteAllCategory() {
        categoryEntityQueries.deleteAll()
    }

    override suspend fun categoryById(id: UUID): Category {
        val result = categoryEntityQueries.getById(1).executeAsOne()
        return Category(
            id = UUID.fromString(""),//result.id,
            name = result.name.toString()
        )
    }

    override fun getCategoryByName(name: String): Category {
        val result = categoryEntityQueries.getByName(name).executeAsOne()
        return Category(
            id = UUID.fromString(""),//result.id,
            name = result.name.toString()
        )

    }

    override fun getAllCategory(): Flow<List<Category>> {
        return categoryEntityQueries.selectAll().asFlow().map {
            it.executeAsList().map {
                Category(
                    id = UUID.fromString(""),//it.id,
                    name = it.name.toString()
                )
            }
        }
    }

    override fun getAllCategoryWithTask(): Flow<List<CategoryWithTask>> {
        TODO("Not yet implemented")
    }

    override fun getAllCategoryWithTaskCount(): Flow<List<CategoryTask>> {
        return categoryEntityQueries.getWithTaskCount().asFlow().map {
            it.executeAsList().map {
                CategoryTask(
                    id = it.id.toString(),
                    name = it.name.toString(),
                    taskCount = it.task_count.toInt()
                )
            }
        }
    }

    override fun getCategoryById(id: UUID): Flow<CategoryWithTask> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCategory(category: Category): Unit {
        categoryEntityQueries.insertOrRelace(
            id = 1,
            name = category.name
        )
    }
}