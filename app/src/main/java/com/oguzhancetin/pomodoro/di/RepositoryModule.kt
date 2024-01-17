package com.oguzhancetin.pomodoro.di

import com.oguzhancetin.pomodoro.core.database.dao.CategoryDao
import com.oguzhancetin.pomodoro.core.database.dao.PomodoroDao
import com.oguzhancetin.pomodoro.core.database.dao.TaskItemDao
import com.oguzhancetin.pomodoro.data.repository.CategoryRepositoryImpl
import com.oguzhancetin.pomodoro.data.repository.PomodoroRepositoryImpl
import com.oguzhancetin.pomodoro.data.repository.TaskItemRepositoryImpl
import com.oguzhancetin.pomodoro.data.repository.CategoryRepository
import com.oguzhancetin.pomodoro.data.repository.PomodoroRepository
import com.oguzhancetin.pomodoro.data.repository.TaskItemRepository

import javax.inject.Singleton

/**
 * Created by ocetin00 on 17.02.2023
 */
/*
@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideTaskItemRepository(taskItemDao: TaskItemDao): TaskItemRepository {
        return TaskItemRepositoryImpl(taskItemDao)
    }

    @Singleton
    @Provides
    fun providePomodoroRepository(pomodoroDao: PomodoroDao): PomodoroRepository {
        return PomodoroRepositoryImpl(pomodoroDao)
    }

    @Singleton
    @Provides
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao)
    }

}*/
