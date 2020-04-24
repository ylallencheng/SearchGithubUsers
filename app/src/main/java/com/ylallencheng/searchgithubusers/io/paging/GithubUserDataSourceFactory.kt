package com.ylallencheng.searchgithubusers.io.paging

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ylallencheng.searchgithubusers.io.service.GithubService
import com.ylallencheng.searchgithubusers.io.model.User
import kotlinx.coroutines.CoroutineScope

class GithubUserDataSourceFactory(private val mService: GithubService,
                                  private val mContext: Context,
                                  private val mScope: CoroutineScope,
                                  private val mQuery: String) : DataSource.Factory<Int, User>() {

    val dataSourceLiveData = MutableLiveData<GithubUserDataSource>()

    override fun create(): DataSource<Int, User> {
        val dataSource = GithubUserDataSource(mService, mContext, mScope, mQuery)
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}