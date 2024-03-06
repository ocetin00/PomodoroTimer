package com.oguzhancetin.pomodoro

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.junit.Assert
import org.junit.Test

class CategoryTest {

    private lateinit var driver: SqlDriver
    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        PomodoroDb.Schema.create(this)
    }

    private val queriesCategory = PomodoroDb(inMemorySqlDriver).categoryEntityQueries
    private val queriesTaskItem = PomodoroDb(inMemorySqlDriver).taskItemEntityQueries


    @Test
    fun categoryInsertTest() = run {
        val emptyItems = queriesCategory.selectAll().executeAsList()
        Assert.assertEquals(emptyItems.size, 0)
        queriesCategory.insertOrRelace(
            id = 1,
            name = "category1"
        )
        val items = queriesCategory.selectAll().executeAsList()
        Assert.assertEquals(items.size, 1)
    }


    @Test
    fun categoryDeleteByIdTest() = run {

        queriesCategory.insertOrRelace(
            id = 1,
            name = "category1"
        )
        val items = queriesCategory.selectAll().executeAsList()
        Assert.assertEquals(items.size, 1)

        queriesCategory.delete(items[0].id)
        val itemsAfterDelete = queriesCategory.selectAll().executeAsList()
        Assert.assertEquals(itemsAfterDelete.size, 0)
    }

    @Test
    fun categoryUpdateTest() = run {
        queriesCategory.insertOrRelace(
            id = null,
            name = "category1"
        )
        val items = queriesCategory.selectAll().executeAsList()
        Assert.assertEquals(items.size, 1)

        val categoryNewName = "category2"
        queriesCategory.insertOrRelace(
            id = items[0].id,
            name = categoryNewName
        )
        val items2 = queriesCategory.selectAll().executeAsList()
        Assert.assertEquals(items2[0].name, categoryNewName)
    }

    @Test
    fun deleteAllCategoryTest() = run {
        queriesCategory.insertOrRelace(
            id = 1,
            name = "category1"
        )
        queriesCategory.insertOrRelace(
            id = 2,
            name = "category2"
        )
        var items = queriesCategory.selectAll().executeAsList()
        Assert.assertEquals(items.size, 2)

        queriesCategory.deleteAll()
        items = queriesCategory.selectAll().executeAsList()
        Assert.assertEquals(items.size, 0)
    }

    @Test
    fun getAllCategoryWithTaskCount() = run {
        queriesCategory.insertOrRelace(
            id = 1,
            name = "category1"
        )
        queriesCategory.insertOrRelace(
            id = 2,
            name = "category2"
        )
        queriesTaskItem.insertOrRelace(
            id = 1,
            description = "task1",
            categoryId = 1,
            is_finished = 0,
            is_favorite = 0,
            create_date = null,
            done_date = null
        )
        queriesTaskItem.insertOrRelace(
            id = 2,
            description = "task2",
            categoryId = 1,
            is_finished = 0,
            is_favorite = 0,
            create_date = null,
            done_date = null
        )
        queriesTaskItem.insertOrRelace(
            id = 3,
            description = "task3",
            categoryId = 2,
            is_finished = 0,
            is_favorite = 0,
            create_date = null,
            done_date = null
        )

        val result = queriesCategory.getWithTaskCount().executeAsList()
        print(result)
    }


}