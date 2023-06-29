package com.oguzhancetin.pomodoro.domain.use_case.task

import com.oguzhancetin.pomodoro.core.model.TaskItem
import com.oguzhancetin.pomodoro.data.repository.TaskItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWeekDoneTaskItems @Inject constructor(
    private val taskItemRepository: TaskItemRepository
) {
    operator fun invoke(currentWeekMilist: Long): Flow<List<TaskItem>> {
        return taskItemRepository.getCurrentWeekTaskList(currentWeekMilist)
    }
}