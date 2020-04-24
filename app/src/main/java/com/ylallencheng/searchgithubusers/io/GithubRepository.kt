package com.ylallencheng.searchgithubusers.io

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.ylallencheng.searchgithubusers.io.model.PagingStatus
import com.ylallencheng.searchgithubusers.io.model.User
import com.ylallencheng.searchgithubusers.io.paging.GithubUserDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GithubRepository @Inject constructor(private val mGithubService: GithubService) {

    fun searchGithubUsers(context: Context,
                          viewModelScope: CoroutineScope,
                          query: String): PagingStatus<User> {

        val factory = GithubUserDataSourceFactory(mGithubService, context, viewModelScope, query)
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(30)
                .setPrefetchDistance(5)
                .build()
        return PagingStatus(
                pagedList = factory.toLiveData(config),
                requestStatus = factory.dataSourceLiveData.switchMap { it.requestStatus })
    }
}