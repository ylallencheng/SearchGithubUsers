package com.ylallencheng.searchgithubusers.io.util

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class PagingStatus<T>(val pagedList: LiveData<PagedList<T>>,
                           val requestStatus: LiveData<RequestStatus>)