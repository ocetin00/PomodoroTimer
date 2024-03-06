package com.oguzhancetin.pomodoro.data.repository

import com.oguzhancetin.pomodoro.core.database.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.core.model.Category
import com.oguzhancetin.pomodoro.core.model.CategoryTask
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CategoryRepository {
    suspend fun upsertCategory(category: Category)
    suspend fun deleteCategory(category: Category)

    suspend fun deleteAllCategory()

    suspend fun categoryById(id: UUID): Category

    fun getCategoryByName(name: String): Category

    fun getAllCategory(): Flow<List<Category>>

    fun getAllCategoryWithTask(): Flow<List<CategoryWithTask>>

    fun getAllCategoryWithTaskCount(): Flow<List<CategoryTask>>
    fun getCategoryById(id: UUID): Flow<CategoryWithTask>

    suspend fun updateCategory(category: Category): Unit

}