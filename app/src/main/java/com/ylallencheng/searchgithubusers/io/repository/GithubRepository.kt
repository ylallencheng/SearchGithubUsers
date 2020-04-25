package com.ylallencheng.searchgithubusers.io.repository

import android.content.Context
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.ylallencheng.searchgithubusers.io.service.GithubService
import com.ylallencheng.searchgithubusers.io.util.PagingStatus
import com.ylallencheng.searchgithubusers.io.model.User
import com.ylallencheng.searchgithubusers.io.pagination.GithubUserDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * The repository served as Model layer in MVVM pattern.
 *
 * @property mGithubService Github service for network requesting
 */
class GithubRepository @Inject constructor(private val mGithubService: GithubService) {

    /**
     * Search Github users.
     *
     * @param context App context for checking network availability
     * @param viewModelScope The scope where network jobs are executed
     * @param query The search query
     */
    fun searchGithubUsers(context: Context,
                          viewModelScope: CoroutineScope,
                          query: String): PagingStatus<User> {

        /*
            create the factory
         */
        val factory = GithubUserDataSourceFactory(mGithubService, context, viewModelScope, query)

        /*
            generate config for pagination
         */
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(30)
                .setPrefetchDistance(5)
                .build()
        /*
            return the wrapped paging status,
            with factory contributes to paged list livedata and request status livedata
         */
        return PagingStatus(
                pagedList = factory.toLiveData(config),
                requestStatus = factory.dataSourceLiveData.switchMap { it.requestStatus })
    }
}