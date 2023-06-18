package com.oguzhancetin.pomodoro.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.oguzhancetin.pomodoro.data.local.PomodoroDatabase
import com.oguzhancetin.pomodoro.data.local.dao.CategoryDao
import com.oguzhancetin.pomodoro.data.local.dao.PomodoroDao
import com.oguzhancetin.pomodoro.data.local.dao.TaskItemDao
import com.oguzhancetin.pomodoro.data.local.entity.CategoryEntity

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.UUID

import javax.inject.Provider
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
        context: Context,
        provider: Provider<CategoryDao>
    ): PomodoroDatabase {
        return Room.databaseBuilder(
            context,
            PomodoroDatabase::class.java,
            "pomodoro_database"
        ).addCallback(
            PersonCallback(provider)
        ).build()
    }


    class PersonCallback(
        private val provider: Provider<CategoryDao>
    ) : RoomDatabase.Callback() {

        private val applicationScope = CoroutineScope(SupervisorJob())

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            applicationScope.launch(Dispatchers.IO) {
                populateDatabase()
            }
        }

        private suspend fun populateDatabase() {
            val category = CategoryEntity(UUID.randomUUID(), "General")
            provider.get().upsert(category)
        }
    }


}