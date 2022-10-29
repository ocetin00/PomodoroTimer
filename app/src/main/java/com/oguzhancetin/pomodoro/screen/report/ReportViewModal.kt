package com.oguzhancetin.pomodoro.screen.report

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import com.oguzhancetin.pomodoro.util.removeDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class ReportViewModal @Inject constructor(private val reportRepository: ReportRepository) : ViewModel(){
    private var _doneTaskItems = MutableStateFlow<ReportUIState>(ReportUIState.Loading("Loading"))
    val doneTaskItems:StateFlow<ReportUIState> =_doneTaskItems

    init {

        //start date of current week
        val calendar: Calendar = Calendar.getInstance()
        Log.d("date_current",calendar.timeInMillis.toString())
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        Log.d("date_current",calendar.timeInMillis.toString())
        calendar.removeDetails()


        viewModelScope.launch {
            Log.d("date",calendar.timeInMillis.toString())
            reportRepository.getDoneTaskItems(currentWeekMillis = calendar.timeInMillis)
                .collect { taskItems ->
                    _doneTaskItems.value = ReportUIState.Success(taskItems)
                }
        }


    }

}
sealed class ReportUIState() {
    /**
     * Indicates the operation succeeded.
     */
    class Success(val taskItems: List<TaskItem>) : ReportUIState()

    /**
     * Indicates the operation is going on with a loading message ID.
     *
     * @param loadingMessageId The ID to find the string resource.
     */
    class Loading(val loadingMessageId: String) : ReportUIState()

    /**
     * Indicates the operation failed with an error message ID.
     *
     * @param errorMessageId The ID to find the string resource.
     */
    class Error( val errorMessageId: String) : ReportUIState()
}