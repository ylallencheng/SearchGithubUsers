package com.ylallencheng.searchgithubusers.ui.search

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.ylallencheng.searchgithubusers.io.GithubRepository
import com.ylallencheng.searchgithubusers.io.User
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel() {

    val query: MutableLiveData<String> = MutableLiveData()

    val users: LiveData<PagedList<User>> =
            Transformations.switchMap(query) {
                repository.searchGithubUsers(viewModelScope = viewModelScope, query = it)
            }
}