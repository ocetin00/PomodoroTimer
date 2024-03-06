package com.oguzhancetin.pomodoro.data.repository

import com.oguzhancetin.pomodoro.core.database.dao.CategoryDao
import com.oguzhancetin.pomodoro.core.database.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.core.mapper.toCategory
import com.oguzhancetin.pomodoro.core.mapper.toCategoryEntity
import com.oguzhancetin.pomodoro.core.mapper.toMapTaskItem
import com.oguzhancetin.pomodoro.core.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class CategoryRepositoryImpl constructor(private val categoryDao: CategoryDao) :
    CategoryRepository {
    override suspend fun upsertCategory(category: Category) {
        categoryDao.upsert(category.toCategoryEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.delete(category.toCategoryEntity())
    }

    override suspend fun deleteAllCategory() {
        categoryDao.deleteAllCategory()
    }

    override suspend fun categoryById(id: UUID): Category {
        return categoryDao.categoryById(id).toCategory()
    }

    override fun getCategoryByName(name: String): Category {
        return categoryDao.getCategoryByName(name).toCategory()

    }

    override fun getAllCategory(): Flow<List<Category>> {
        return categoryDao.getAllCategory()
            .map {
                it.map { it.toCategory() }
            }

    }

    override fun getAllCategoryWithTask(): Flow<List<CategoryWithTask>> {
        return categoryDao.getAllCategoryWithTask()
            .map {
                it.map { categoryWithTaskItem ->
                    categoryWithTaskItem.apply {
                        this.category.toCategory()
                        this.taskList.map { task -> task.toMapTaskItem() }
                    }
                }
            }
    }

    override fun getCategoryById(id: UUID): Flow<CategoryWithTask> {
        return categoryDao.getCategoryById(id)
            .map { categoryWithTaskItem ->
                categoryWithTaskItem.apply {
                    this.category.toCategory()
                    this.taskList.map { task -> task.toMapTaskItem() }
                }
            }
    }

    override suspend fun updateCategory(category: Category): Flow<Any> {
        return flow {
            categoryDao.updateCategory(category.toCategoryEntity())
        }
    }
}