package com.oguzhancetin.pomodoro.data.mapper

import com.oguzhancetin.pomodoro.data.local.entity.PomodoroEntity
import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import com.oguzhancetin.pomodoro.util.Times

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