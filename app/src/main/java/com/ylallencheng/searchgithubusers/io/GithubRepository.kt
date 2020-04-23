package com.ylallencheng.searchgithubusers.io

import javax.inject.Inject

class GithubRepository @Inject constructor(private val service: GithubService) {
    var page = 1
    var pageLimit = 50


    fun searchGithubUsers(query: String) = service.searchUsers(query, page, pageLimit)
}