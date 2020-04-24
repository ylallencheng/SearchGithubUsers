package com.ylallencheng.searchgithubusers.io

import com.ylallencheng.searchgithubusers.io.model.SearchUsersRs
import retrofit2.http.GET
import retrofit2.http.Query

/* ------------------------------ Service */

interface GithubService {

    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String,
                            @Query("page") page: Int,
                            @Query("page_limit") pageLimit: Int): SearchUsersRs
}