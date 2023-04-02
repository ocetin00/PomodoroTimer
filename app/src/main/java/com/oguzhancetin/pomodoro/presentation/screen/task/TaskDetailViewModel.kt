package com.oguzhancetin.pomodoro.presentation.screen.task

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel
@Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val userId: String = checkNotNull(savedStateHandle["userId"])

    init {
        Log.d("ViewModelTaskDetail", UUID.fromString(userId).toString())
    }

}