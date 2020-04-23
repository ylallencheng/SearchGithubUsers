package com.ylallencheng.searchgithubusers.io

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ylallencheng.searchgithubusers.io.paging.GithubUserDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val githubService: GithubService
) {
    fun searchGithubUsers(
        viewModelScope: CoroutineScope,
        query: String
    ): LiveData<PagedList<User>> {
        val dataSourceFactory = GithubUserDataSourceFactory(viewModelScope, githubService, query)
        val dataSourceLiveData = dataSourceFactory.liveDataSource

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(30)
            .setPrefetchDistance(5)
            .build()

        return LivePagedListBuilder(dataSourceFactory, config).build()
    }
}