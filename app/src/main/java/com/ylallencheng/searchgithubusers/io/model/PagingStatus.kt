package com.ylallencheng.searchgithubusers.io.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class PagingStatus<T>(val pagedList: LiveData<PagedList<T>>,
                           val requestStatus: LiveData<RequestStatus>)