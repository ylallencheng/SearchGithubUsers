package com.ylallencheng.searchgithubusers.di.search

import com.ylallencheng.searchgithubusers.ui.search.SearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SearchActivityModule {

    @SearchScope
    @ContributesAndroidInjector(modules = [SearchModule::class])
    abstract fun contributesSearchActivity(): SearchActivity
}