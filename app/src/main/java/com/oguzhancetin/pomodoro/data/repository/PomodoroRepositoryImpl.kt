package com.oguzhancetin.pomodoro.data.repository

import com.oguzhancetin.pomodoro.core.database.dao.PomodoroDao
import com.oguzhancetin.pomodoro.core.mapper.toPomodoro
import com.oguzhancetin.pomodoro.core.mapper.toPomodoroEntity
import com.oguzhancetin.pomodoro.core.model.Pomodoro
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by ocetin00 on 12.02.2023
 */
class PomodoroRepositoryImpl @Inject constructor(private val pomodoroDao: PomodoroDao) :
    PomodoroRepository {
    override fun getAllPomodoro(): Flow<List<Pomodoro>> {
        return pomodoroDao.getAllPomodoro()
            .map {
                it.map { m -> m.toPomodoro() }
            }

    }

    override fun getCurrentWeekPomodoroList(currentWeekMilist: Long): Flow<List<Pomodoro>> {
        return pomodoroDao.finishPomodoroListForCurrentWeek(currentWeekMilist)
            .map {
                it.map { m -> m.toPomodoro() }
            }
    }

    override fun getPomodoroItemsByFinish(isFinish: Int): Flow<List<Pomodoro>> {
        return pomodoroDao.pomodoroListByFinish(isFinish)
            .map {
                it.map { m -> m.toPomodoro() }
            }


    }

    override suspend fun deletePomodoro(pomodoro: Pomodoro) {
        pomodoroDao.delete(pomodoro.toPomodoroEntity())
    }

    override suspend fun insertPomodoro(pomodoro: Pomodoro) {
        pomodoroDao.insert(pomodoro.toPomodoroEntity())
    }

    override fun getPomodoroById(id: Int): Flow<Pomodoro> {
        return pomodoroDao.getPomodoroById(id)
            .map {
                it.toPomodoro()
            }
    }

    override suspend fun updatePomodoro(pomodoro: Pomodoro) {
        pomodoroDao.updatePomodoro(pomodoro.toPomodoroEntity())
    }

    override suspend fun isAnyPomodoroFinish(): Boolean {
        return pomodoroDao.isAnyPomodoroFinish() == 1
    }
}