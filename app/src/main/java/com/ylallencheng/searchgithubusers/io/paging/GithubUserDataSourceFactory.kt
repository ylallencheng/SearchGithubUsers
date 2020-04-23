package com.ylallencheng.searchgithubusers.io.paging

import androidx.paging.DataSource
import com.ylallencheng.searchgithubusers.io.User
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GithubUserDataSourceFactory @Inject constructor(
    private val githubUserDataSource: GithubUserDataSource
) : DataSource.Factory<Int, User>() {

    override fun create(): DataSource<Int, User> {
        return githubUserDataSource
    }

    fun scope(scope: CoroutineScope) {
        githubUserDataSource.scope = scope
    }

    fun query(query: String) {
        githubUserDataSource.query = query
    }
}