package com.oguzhancetin.pomodoro.di

import android.content.Context
import androidx.room.Room
import com.oguzhancetin.pomodoro.data.PomodoroDatabase
import com.oguzhancetin.pomodoro.data.local.TaskItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideTaskItemDao(db: PomodoroDatabase): TaskItemDao {
        return db.taskItemDao()
    }

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