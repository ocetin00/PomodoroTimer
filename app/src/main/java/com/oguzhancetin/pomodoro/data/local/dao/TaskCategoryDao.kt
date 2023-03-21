package com.oguzhancetin.pomodoro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.oguzhancetin.pomodoro.data.local.entity.CategoryEntity
import com.oguzhancetin.pomodoro.data.local.entity.CategoryWithTask

@Dao
interface TaskCategoryDao {

    @Transaction
    @Query("SELECT * FROM task_category")
    fun getCategoryWithTask(): List<CategoryWithTask>


    @Query("Select * from task_category where name= :name")
    fun getCategoryByName(name:String): CategoryEntity

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(categoryEntity: CategoryEntity)


}