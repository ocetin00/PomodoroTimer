package com.oguzhancetin.pomodoro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by ocetin00 on 12.02.2023
 */
@Entity(tableName = "pomodoro")
data class PomodoroEntity(
    @PrimaryKey
    val id: UUID,
    @ColumnInfo(name = "created_date")
    val createdDate: Long,

    )