package com.oguzhancetin.pomodoro.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.oguzhancetin.pomodoro.core.database.PomodoroDatabase
import com.oguzhancetin.pomodoro.core.database.dao.CategoryDao
import com.oguzhancetin.pomodoro.core.database.dao.TaskItemDao
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PomodoroRepositoryImplTest {

    private lateinit var taskCategoryDao: CategoryDao
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
    /*
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

            val result = taskCategoryDao.getAllCategoryWithTask()[0]
            val expected = CategoryWithTask(category,task)

            assertEquals(result.category,expected.category)
            assertEquals(result.taskList,expected.taskList)
        }*/
}