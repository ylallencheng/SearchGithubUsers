package com.ylallencheng.searchgithubusers.io.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ylallencheng.searchgithubusers.io.GithubService
import com.ylallencheng.searchgithubusers.io.User
import kotlinx.coroutines.CoroutineScope

class GithubUserDataSourceFactory(
    private val scope: CoroutineScope,
    private val service: GithubService,
    private val query: String
) : DataSource.Factory<Int, User>() {

    lateinit var dataSource: GithubUserDataSource
    val liveDataSource: MutableLiveData<GithubUserDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, User> {
        dataSource = GithubUserDataSource(scope, service, query)
        liveDataSource.postValue(dataSource)
        return dataSource
    }

}