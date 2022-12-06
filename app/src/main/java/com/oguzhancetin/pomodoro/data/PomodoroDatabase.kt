package com.oguzhancetin.pomodoro.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oguzhancetin.pomodoro.data.local.TaskItemDao
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem

@Database(entities = [TaskItem::class], exportSchema = true, version = 5)
abstract class PomodoroDatabase: RoomDatabase() {
    abstract fun taskItemDao(): TaskItemDao
}