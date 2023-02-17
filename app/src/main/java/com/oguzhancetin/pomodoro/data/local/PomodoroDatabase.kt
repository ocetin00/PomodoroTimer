package com.oguzhancetin.pomodoro.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oguzhancetin.pomodoro.data.local.dao.PomodoroDao
import com.oguzhancetin.pomodoro.data.local.dao.TaskItemDao
import com.oguzhancetin.pomodoro.data.local.entity.PomodoroEntity
import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity

@Database(entities = [TaskItemEntity::class, PomodoroEntity::class], exportSchema = true, version = 10)
abstract class PomodoroDatabase: RoomDatabase() {
    abstract fun taskItemDao(): TaskItemDao
    abstract fun pomodoroDao(): PomodoroDao
}