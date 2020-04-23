package com.ylallencheng.searchgithubusers.io.paging

import androidx.paging.PageKeyedDataSource
import com.ylallencheng.searchgithubusers.io.GithubService
import com.ylallencheng.searchgithubusers.io.SearchUsersRs
import com.ylallencheng.searchgithubusers.io.User
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubUserDataSource constructor(
    private val scope: CoroutineScope,
    private val service: GithubService,
    private val query: String
) : PageKeyedDataSource<Int, User>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>
    ) {
        val call = service.searchUsers(query, page = 1, pageLimit = params.requestedLoadSize)
        call.enqueue(object : Callback<SearchUsersRs> {

            override fun onResponse(
                call: Call<SearchUsersRs>,
                response: Response<SearchUsersRs>
            ) {
                val rs = response.body()
                callback.onResult(rs?.items ?: listOf(), null, 2)
            }

            override fun onFailure(
                call: Call<SearchUsersRs>,
                t: Throwable
            ) {
            }
        })
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, User>
    ) {
        val call =
            service.searchUsers(query, page = params.key, pageLimit = params.requestedLoadSize)
        call.enqueue(object : Callback<SearchUsersRs> {

            override fun onResponse(
                call: Call<SearchUsersRs>,
                response: Response<SearchUsersRs>
            ) {
                val rs = response.body()
                callback.onResult(rs?.items ?: listOf(), params.key + 1)
            }

            override fun onFailure(
                call: Call<SearchUsersRs>,
                t: Throwable
            ) {
            }
        })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, User>
    ) {
    }
}