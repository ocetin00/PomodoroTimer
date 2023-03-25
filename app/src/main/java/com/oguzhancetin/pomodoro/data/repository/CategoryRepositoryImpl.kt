package com.oguzhancetin.pomodoro.data.repository

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.data.local.dao.CategoryDao
import com.oguzhancetin.pomodoro.data.local.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.data.mapper.toCategory
import com.oguzhancetin.pomodoro.data.mapper.toCategoryEntity
import com.oguzhancetin.pomodoro.data.mapper.toMapTaskItem
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryDao: CategoryDao) :
    CategoryRepository {
    override suspend fun insertCategory(category: Category) {
        categoryDao.insert(category.toCategoryEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.delete(category.toCategoryEntity())
    }

    override suspend fun deleteAllCategory() {
        categoryDao.deleteAllCategory()
    }

    override fun getCategoryByName(name: String): Flow<Resource<Category>> {
        return categoryDao.getCategoryByName(name)
            .onStart {
                Resource.Loading<Category>()
            }
            .map {
                Resource.Success(it.toCategory())
            }
            .catch { e ->
                if (e is IOException) Resource.Error(e.message ?: "An error occured", null)
            }

    }

    override fun getAllCategory(): Flow<Resource<List<Category>>> {
        return categoryDao.getAllCategory()
            .onStart {
                Resource.Loading<List<Category>>()
            }
            .map {
                Resource.Success(it.map { it.toCategory() })
            }
            .catch { e ->
                if (e is IOException) Resource.Error(e.message ?: "An error occured", null)
            }
    }

    override fun getAllCategoryWithTask(): Flow<Resource<List<CategoryWithTask>>> {
        return categoryDao.getAllCategoryWithTask()
            .onStart {
                Resource.Loading<List<CategoryWithTask>>()
            }
            .map {
                Resource.Success(it.map { categoryWithTaskItem ->
                    categoryWithTaskItem.apply {
                        this.category.toCategory()
                        this.taskList.map { task -> task.toMapTaskItem() }
                    }
                })
            }
            .catch { e ->
                if (e is IOException) Resource.Error(e.message ?: "An error occured", null)
            }
    }

    override fun getCategoryById(id: UUID): Flow<Resource<CategoryWithTask>> {
        return categoryDao.getCategoryById(id)
            .onStart {
                Resource.Loading<CategoryWithTask>()
            }
            .map { categoryWithTaskItem ->
                Resource.Success(
                    categoryWithTaskItem.apply {
                        this.category.toCategory()
                        this.taskList.map { task -> task.toMapTaskItem() }

                    })
            }
            .catch { e ->
                if (e is IOException) Resource.Error(e.message ?: "An error occured", null)
            }

    }
}