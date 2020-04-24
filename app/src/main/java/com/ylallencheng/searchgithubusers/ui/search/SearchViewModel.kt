package com.ylallencheng.searchgithubusers.ui.search

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.ylallencheng.searchgithubusers.SGUApplication
import com.ylallencheng.searchgithubusers.io.GithubRepository
import com.ylallencheng.searchgithubusers.io.model.PagingStatus
import com.ylallencheng.searchgithubusers.io.model.RequestStatus
import com.ylallencheng.searchgithubusers.io.model.User
import javax.inject.Inject

class SearchViewModel @Inject constructor(application: SGUApplication,
                                          private val mRepository: GithubRepository) : AndroidViewModel(application) {

    private val mPagingStatus: MutableLiveData<PagingStatus<User>> = MutableLiveData()

    val users: LiveData<PagedList<User>> =
            mPagingStatus.switchMap { it.pagedList }

    val requestStatus: LiveData<RequestStatus> =
            mPagingStatus.switchMap { it.requestStatus }

    fun searchUsers(query: String) {
        mPagingStatus.value = mRepository.searchGithubUsers(
                context = getApplication<SGUApplication>().applicationContext,
                viewModelScope = viewModelScope,
                query = query)
    }
}