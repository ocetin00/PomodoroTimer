package com.oguzhancetin.pomodoro.common

/**
 * Created by ocetin00 on 15.01.2023
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    class Loading<T>: Resource<T>(null)
}