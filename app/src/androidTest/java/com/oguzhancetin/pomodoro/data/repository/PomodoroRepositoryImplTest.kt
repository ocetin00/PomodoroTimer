package com.oguzhancetin.pomodoro.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oguzhancetin.pomodoro.data.local.PomodoroDatabase
import com.oguzhancetin.pomodoro.data.local.dao.TaskCategoryDao
import com.oguzhancetin.pomodoro.data.local.dao.TaskItemDao
import com.oguzhancetin.pomodoro.data.local.entity.CategoryEntity
import com.oguzhancetin.pomodoro.data.local.entity.CategoryWithTask
import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class PomodoroRepositoryImplTest {

    private lateinit var taskCategoryDao: TaskCategoryDao
    private lateinit var taskItemDao: TaskItemDao
    private lateinit var db: PomodoroDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, PomodoroDatabase::class.java
        ).build()
        taskItemDao = db.taskItemDao()
        taskCategoryDao = db.taskCategoryDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runTest{
        val cId = UUID.randomUUID()
        val category = CategoryEntity(cId,"c1")

         taskCategoryDao.insert(category)
        val resultCategory = taskCategoryDao.getCategoryByName("c1")

        val task = listOf(
            TaskItemEntity(
                id = UUID.randomUUID(),
                description = "a",
                categoryId = resultCategory.id
            ),
            TaskItemEntity(
                id = UUID.randomUUID(),
                description = "a",
                categoryId = resultCategory.id
            )
        )
        task.forEach {
            taskItemDao.insert(it)
        }

        val result = taskCategoryDao.getCategoryWithTask()[0]
        val expected = CategoryWithTask(category,task)

        assertEquals(result.category,expected.category)
        assertEquals(result.taskList,expected.taskList)
    }
}