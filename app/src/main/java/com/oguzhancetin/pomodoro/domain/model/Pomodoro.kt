package com.oguzhancetin.pomodoro.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by ocetin00 on 12.02.2023
 */
data class Pomodoro(
    val id: UUID,
    val createdDate: Long,
)