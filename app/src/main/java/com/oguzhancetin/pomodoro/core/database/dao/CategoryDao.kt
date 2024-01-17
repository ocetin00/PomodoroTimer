package com.oguzhancetin.pomodoro.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.oguzhancetin.pomodoro.core.database.entity.CategoryEntity
import com.oguzhancetin.pomodoro.core.database.entity.relation.CategoryWithTask
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CategoryDao {

    @Transaction
    @Query("SELECT * FROM task_category")
    fun getAllCategoryWithTask(): Flow<List<CategoryWithTask>>


    @Query("SELECT * FROM task_category")
    fun getAllCategory(): Flow<List<CategoryEntity>>
    @Query("DELETE FROM TASK_CATEGORY")
    fun deleteAllCategory()

    @Delete
    fun delete(categoryEntity: CategoryEntity)

    @Transaction
    @Query("SELECT * FROM TASK_CATEGORY WHERE id = :id")
    fun getCategoryById(id: UUID): Flow<CategoryWithTask>

    @Query("SELECT * FROM TASK_CATEGORY WHERE id = :id")
    suspend fun categoryById(id: UUID): CategoryEntity

    @Query("Select * from task_category where name= :name")
    fun getCategoryByName(name:String): Flow<CategoryEntity>

    @Insert
    suspend fun upsert(categoryEntity: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)



}