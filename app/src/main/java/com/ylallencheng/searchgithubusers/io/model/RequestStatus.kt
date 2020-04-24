package com.ylallencheng.searchgithubusers.io.model

data class RequestStatus(val status: Status,
                         val errorMessage: String? = null)

enum class Status {
    LOADING,
    SUCCESS,
    REFRESHING,
    FAILED
}