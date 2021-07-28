package com.example.picture.photo.domain

/*
This has been copied from the Android paging library sample
 */

enum class Status {
    LOADING,
    SUCCESS,
    FAILURE
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null
) {
    companion object {
        val SUCCESS = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.LOADING)
        fun error(msg: String?) = NetworkState(Status.FAILURE, msg)
    }
}
