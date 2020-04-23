package com.ylallencheng.searchgithubusers.di.search

import androidx.lifecycle.ViewModel
import com.ylallencheng.searchgithubusers.di.viewModel.ViewModelKey
import com.ylallencheng.searchgithubusers.ui.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(value = SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel
}