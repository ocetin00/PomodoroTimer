package com.oguzhancetin.pomodoro.screen.report

import com.oguzhancetin.pomodoro.data.local.TaskItemDao
import javax.inject.Inject

class ReportRepository @Inject constructor(private val taskItemDao: TaskItemDao) {
    /**
     * Get pomodoro items that is done after current week first date
     */
    fun getDoneTaskItems(currentWeekMillis:Long) = taskItemDao.doneTaskItems(current_week_millis = currentWeekMillis)
}