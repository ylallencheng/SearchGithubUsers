package com.ylallencheng.searchgithubusers.ui.search

import androidx.lifecycle.ViewModel
import com.ylallencheng.searchgithubusers.io.GithubRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel() {
}