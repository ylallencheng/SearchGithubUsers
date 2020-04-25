package com.ylallencheng.searchgithubusers.io.util

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ylallencheng.searchgithubusers.io.pagination.GithubUserDataSourceFactory

/**
 * This is the wrapped class for handling pagination with retrofit's network status monitoring.
 *
 * @property pagedList The paged list created by [GithubUserDataSourceFactory] wrapped in live data
 * @property requestStatus The request status of retrofit's network
 */
data class PagingStatus<T>(val pagedList: LiveData<PagedList<T>>,
                           val requestStatus: LiveData<RequestStatus>)