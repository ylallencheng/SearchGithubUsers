package com.ylallencheng.searchgithubusers.di.search

import androidx.paging.PagedList
import com.ylallencheng.searchgithubusers.io.GithubRepository
import com.ylallencheng.searchgithubusers.io.GithubService
import com.ylallencheng.searchgithubusers.io.paging.GithubUserDataSource
import com.ylallencheng.searchgithubusers.io.paging.GithubUserDataSourceFactory
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    @SearchScope
    fun provideGithubRepository(
        githubUserDataSourceFactory: GithubUserDataSourceFactory,
        config: PagedList.Config
    ): GithubRepository =
        GithubRepository(githubUserDataSourceFactory, config)

    @Provides
    @SearchScope
    fun provideGithubUserDataSourceFactory(githubUserDataSource: GithubUserDataSource): GithubUserDataSourceFactory =
        GithubUserDataSourceFactory(githubUserDataSource)

    @Provides
    @SearchScope
    fun provideGithubUserDataSource(githubService: GithubService): GithubUserDataSource =
        GithubUserDataSource(githubService)

    @Provides
    @SearchScope
    fun provideGithubUserDataSourcePagedListConfig(): PagedList.Config =
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(30)
            .setPrefetchDistance(5)
            .build()
}