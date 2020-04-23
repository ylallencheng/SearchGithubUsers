package com.ylallencheng.searchgithubusers.di.search

import com.ylallencheng.searchgithubusers.io.GithubRepository
import com.ylallencheng.searchgithubusers.io.GithubService
import com.ylallencheng.searchgithubusers.io.paging.GithubUserDataSource
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    @SearchScope
    fun provideGithubRepository(githubService: GithubService): GithubRepository =
        GithubRepository(githubService)

//    @Provides
//    @SearchScope
//    fun provideGithubUserDataSource(githubService: GithubService): GithubUserDataSource =
//        GithubUserDataSource(githubService)
}