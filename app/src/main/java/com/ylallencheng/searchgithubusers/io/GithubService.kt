package com.ylallencheng.searchgithubusers.io

import com.squareup.moshi.Json
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/* ------------------------------ Service */

interface GithubService {

    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Call<SearchUsersRs>
}

/* ------------------------------ API Model */

data class SearchUsersRs(
    @Json(name = "total_count") val totalCount: Int,
    @Json(name = "incomplete_results") val incompleteResults: Boolean,
    val items: List<User>
)

data class User(
    @Json(name = "login") val username: String?,
    @Json(name = "avatar_url") val avatarUrl: String?
)