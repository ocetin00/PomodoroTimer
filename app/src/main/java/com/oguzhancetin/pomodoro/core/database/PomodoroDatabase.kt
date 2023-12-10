package com.oguzhancetin.pomodoro.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.oguzhancetin.pomodoro.core.database.dao.CategoryDao
import com.oguzhancetin.pomodoro.core.database.dao.PomodoroDao
import com.oguzhancetin.pomodoro.core.database.dao.TaskItemDao
import com.oguzhancetin.pomodoro.core.database.entity.CategoryEntity
import com.oguzhancetin.pomodoro.core.database.entity.PomodoroEntity
import com.oguzhancetin.pomodoro.core.database.entity.TaskItemEntity


@Database(entities = [TaskItemEntity::class, PomodoroEntity::class, CategoryEntity::class], exportSchema = true, version = 16)
abstract class PomodoroDatabase: RoomDatabase() {

    abstract fun taskItemDao(): TaskItemDao
    abstract fun pomodoroDao(): PomodoroDao
    abstract fun taskCategoryDao(): CategoryDao
}