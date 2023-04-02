package com.oguzhancetin.pomodoro.di

import android.content.Context
import androidx.room.Room
import com.oguzhancetin.pomodoro.data.local.PomodoroDatabase
import com.oguzhancetin.pomodoro.data.local.dao.CategoryDao
import com.oguzhancetin.pomodoro.data.local.dao.PomodoroDao
import com.oguzhancetin.pomodoro.data.local.dao.TaskItemDao
import com.oguzhancetin.pomodoro.data.repository.PomodoroRepositoryImpl
import com.oguzhancetin.pomodoro.data.repository.TaskItemRepositoryImpl
import com.oguzhancetin.pomodoro.domain.repository.PomodoroRepository
import com.oguzhancetin.pomodoro.domain.repository.TaskItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideTaskItemDao(db: PomodoroDatabase): TaskItemDao {
        return db.taskItemDao()
    }
    @Singleton
    @Provides
    fun providePomodoroDao(db: PomodoroDatabase): PomodoroDao {
        return db.pomodoroDao()
    }
    @Singleton
    @Provides
    fun provideCategoryDao(db: PomodoroDatabase): CategoryDao {
        return db.taskCategoryDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext
        context: Context
    ): PomodoroDatabase {
        return Room.databaseBuilder(
            context,
            PomodoroDatabase::class.java,
            "pomodoro_database"
        ).fallbackToDestructiveMigration().build()
    }



}