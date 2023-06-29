package com.oguzhancetin.pomodoro.core.mapper

import com.oguzhancetin.pomodoro.core.database.entity.PomodoroEntity
import com.oguzhancetin.pomodoro.core.model.Pomodoro

/**
 * Created by ocetin00 on 12.02.2023
 */


fun PomodoroEntity.toPomodoro(): Pomodoro {
    return Pomodoro(
        id = this.id,
        doneDate = this.doneDate
    )
}

fun Pomodoro.toPomodoroEntity(): PomodoroEntity {
    return PomodoroEntity(
        id = this.id,
        doneDate = this.doneDate
    )
}