package com.oguzhancetin.pomodoro.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.oguzhancetin.pomodoro.data.local.entity.CategoryEntity
import com.oguzhancetin.pomodoro.data.local.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.domain.model.Category
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CategoryDao {

    @Transaction
    @Query("SELECT * FROM task_category")
    fun getAllCategoryWithTask(): Flow<List<CategoryWithTask>>

    @Transaction
    @Query("SELECT * FROM task_category")
    fun getAllCategory(): Flow<List<CategoryEntity>>
    @Query("DELETE FROM TASK_CATEGORY")
    fun deleteAllCategory()

    @Delete
    fun delete(categoryEntity: CategoryEntity)

    @Transaction
    @Query("SELECT * FROM TASK_CATEGORY WHERE id = :id")
    fun getCategoryById(id: UUID): Flow<CategoryWithTask>


    @Query("Select * from task_category where name= :name")
    fun getCategoryByName(name:String): Flow<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(categoryEntity: CategoryEntity)


}