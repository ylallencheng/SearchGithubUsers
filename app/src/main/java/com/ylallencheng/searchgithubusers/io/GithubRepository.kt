package com.ylallencheng.searchgithubusers.io

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ylallencheng.searchgithubusers.io.paging.GithubUserDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val dataSourceFactory: GithubUserDataSourceFactory,
    private val pagedListConfig: PagedList.Config
) {

    fun searchGithubUsers(
        viewModelScope: CoroutineScope,
        query: String
    ): LiveData<PagedList<User>> =
        LivePagedListBuilder(
            dataSourceFactory.apply {
                scope(viewModelScope)
                query(query)
            },
            pagedListConfig
        ).build()

}