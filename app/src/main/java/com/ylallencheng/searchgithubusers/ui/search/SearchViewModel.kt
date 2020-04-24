package com.ylallencheng.searchgithubusers.ui.search

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.ylallencheng.searchgithubusers.SGUApplication
import com.ylallencheng.searchgithubusers.io.repository.GithubRepository
import com.ylallencheng.searchgithubusers.io.util.PagingStatus
import com.ylallencheng.searchgithubusers.io.util.RequestStatus
import com.ylallencheng.searchgithubusers.io.model.User
import com.ylallencheng.searchgithubusers.io.util.Status
import javax.inject.Inject

/**
 * ViewModel served as ..... view model in MVVM...
 */
class SearchViewModel @Inject constructor(application: SGUApplication,
                                          private val mRepository: GithubRepository) : AndroidViewModel(application) {
    /*
        Paging Status
     */
    private val mPagingStatus: MutableLiveData<PagingStatus<User>> = MutableLiveData()

    /*
        paged list of users
     */
    val users: LiveData<PagedList<User>> =
            mPagingStatus.switchMap {
                it.pagedList
            }

    /*
        request status
     */
    val requestStatus: LiveData<RequestStatus> =
            mPagingStatus.switchMap {
                it.requestStatus
            }

    /**
     * Search Github users
     *
     * @param query The search query
     */
    fun searchGithubUsers(query: String) {
        /*
            when query is blank, fire a PagingStatus with failed request status
         */
        if (query.isBlank()) {
            mPagingStatus.value = PagingStatus(
                    pagedList = MutableLiveData(),
                    requestStatus = MutableLiveData<RequestStatus>().apply {
                        value = RequestStatus.failed("Query should not be empty")
                    })

        }

        /*
            otherwise, get PagingStatus from repository
         */
        else {
            mPagingStatus.value = mRepository.searchGithubUsers(
                    context = getApplication<SGUApplication>().applicationContext,
                    viewModelScope = viewModelScope,
                    query = query)
        }
    }
}