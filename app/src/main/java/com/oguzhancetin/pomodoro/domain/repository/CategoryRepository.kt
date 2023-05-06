package com.oguzhancetin.pomodoro.domain.repository

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.data.local.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.domain.model.Category
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CategoryRepository {
    suspend fun upsertCategory(category: Category)
    suspend fun deleteCategory(category: Category)

    suspend fun deleteAllCategory()

    suspend fun categoryById(id:UUID): Category

    fun getCategoryByName(name:String): Flow<Resource<Category>>

    fun getAllCategory(): Flow<Resource<List<Category>>>

    fun getAllCategoryWithTask(): Flow<Resource<List<CategoryWithTask>>>
    fun getCategoryById(id:UUID): Flow<Resource<CategoryWithTask>>

    suspend fun updateCategory(category: Category): Flow<Resource<Any>>



}