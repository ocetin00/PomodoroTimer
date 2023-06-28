package com.oguzhancetin.pomodoro.domain.use_case.task

import com.oguzhancetin.pomodoro.core.Resource
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.repository.TaskItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWeekDoneTaskItems @Inject constructor(
    private val taskItemRepository: TaskItemRepository
) {
    operator fun invoke(currentWeekMilist: Long): Flow<Resource<List<TaskItem>>> {
        return taskItemRepository.getCurrentWeekTaskList(currentWeekMilist)
    }
}