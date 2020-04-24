package com.ylallencheng.searchgithubusers.di.search

import com.ylallencheng.searchgithubusers.io.repository.GithubRepository
import com.ylallencheng.searchgithubusers.io.service.GithubService
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    @SearchScope
    fun provideGithubRepository(githubService: GithubService): GithubRepository =
            GithubRepository(githubService)
}