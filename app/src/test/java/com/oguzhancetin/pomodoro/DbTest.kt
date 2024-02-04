package com.oguzhancetin.pomodoro


import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.oguzhancetin.pomodoro.PomodoroDb.Companion.Schema
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import javax.xml.validation.Schema


class DbTest {


    private lateinit var driver: SqlDriver
    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        Schema.create(this)
    }

    private val queries = PomodoroDb(inMemorySqlDriver).taskItemEntityQueries


    @Test
    fun taskItemInsertTest() = run {
        val emptyItems = queries.selectAll().executeAsList()
        val categoryId = 0
        assertEquals(emptyItems.size, 0)
        queries.insertOrRelace(
            id = null,
            description = "a",
            categoryId = 1,
            is_finished = 0,
            is_favorite = 0,
            create_date = null,
            done_date = null
        )
        val items = queries.selectAll().executeAsList()
        assertEquals(items.size, 1)
    }


    @Test
    fun taskItemDeleteTest() = run {

        queries.insertOrRelace(
            id = null,
            description = "a",
            categoryId = 1,
            is_finished = 0,
            is_favorite = 0,
            create_date = null,
            done_date = null
        )
        val items = queries.selectAll().executeAsList()
        assertEquals(items.size, 1)

        queries.delete(items[0].id)
        val itemsAfterDelete = queries.selectAll().executeAsList()
        assertEquals(itemsAfterDelete.size, 0)
    }

    @Test
    fun taskItemUpdateTest() = run {
        queries.insertOrRelace(
            id = null,
            description = "a",
            categoryId = 1,
            is_finished = 0,
            is_favorite = 0,
            create_date = null,
            done_date = null
        )
        val items = queries.selectAll().executeAsList()
        assertEquals(items.size, 1)

        queries.insertOrRelace(
            id = items[0].id,
            description = "a",
            categoryId = 1,
            is_finished = 0,
            is_favorite = 0,
            create_date = null,
            done_date = null
        )
        val items2 = queries.selectAll().executeAsList()
        assertEquals(items2 .size, 1)
    }


}