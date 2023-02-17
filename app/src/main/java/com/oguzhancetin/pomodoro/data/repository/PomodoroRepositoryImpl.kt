package com.oguzhancetin.pomodoro.data.repository

import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.data.local.dao.PomodoroDao
import com.oguzhancetin.pomodoro.data.local.entity.PomodoroEntity
import com.oguzhancetin.pomodoro.data.mapper.toPomodoro
import com.oguzhancetin.pomodoro.data.mapper.toPomodoroEntity
import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import com.oguzhancetin.pomodoro.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.io.IOException
import javax.inject.Inject

/**
 * Created by ocetin00 on 12.02.2023
 */
class PomodoroRepositoryImpl @Inject constructor(private val pomodoroDao: PomodoroDao) :
    PomodoroRepository {
    override fun getAllPomodoro(): Flow<Resource<List<Pomodoro>>> {
        return pomodoroDao.getAllPomodoro()
            .onStart {
                Resource.Loading<List<PomodoroEntity>>()
            }
            .map {
                Resource.Success(it.map { m -> m.toPomodoro() })
            }
            .catch { e ->
                if (e is IOException) Resource.Error(e.message ?: "An error occured", null)
            }
    }

    override fun getCurrentWeekPomodoroList(currentWeekMilist: Long): Flow<Resource<List<Pomodoro>>> {
        return pomodoroDao.finishPomodoroListForCurrentWeek(currentWeekMilist)
            .onStart {
                Resource.Loading<List<PomodoroEntity>>()
            }
            .map {
                Resource.Success(it.map { m -> m.toPomodoro() })
            }
            .catch { e ->
                if (e is IOException) Resource.Error(e.message ?: "An error occured", null)
            }

    }

    override fun getPomodoroItemsByFinish(isFinish: Int): Flow<Resource<List<Pomodoro>>> {
        return pomodoroDao.pomodoroListByFinish(isFinish)
            .onStart {
                Resource.Loading<List<PomodoroEntity>>()
            }
            .map {
                Resource.Success(it.map { m -> m.toPomodoro() })
            }
            .catch { e ->
                if (e is IOException) Resource.Error(e.message ?: "An error occured", null)
            }

    }

    override suspend fun deletePomodoro(pomodoro: Pomodoro) {
        pomodoroDao.delete(pomodoro.toPomodoroEntity())
    }

    override suspend fun insertPomodoro(pomodoro: Pomodoro) {
        pomodoroDao.insert(pomodoro.toPomodoroEntity())
    }

    override fun getPomodoroById(id: Int): Flow<Resource<Pomodoro>> {
        return pomodoroDao.getPomodoroById(id)
            .onStart {
                Resource.Loading<PomodoroEntity>()
            }
            .map {
                Resource.Success(it.toPomodoro())
            }
            .catch { e ->
                if (e is IOException) Resource.Error(e.message ?: "An error occured", null)
            }
    }

    override suspend fun updatePomodoro(pomodoro: Pomodoro) {
        pomodoroDao.updatePomodoro(pomodoro.toPomodoroEntity())
    }
}