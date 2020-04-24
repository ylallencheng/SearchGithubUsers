package com.ylallencheng.searchgithubusers.io.paging

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.ylallencheng.searchgithubusers.io.GithubService
import com.ylallencheng.searchgithubusers.io.model.User
import kotlinx.coroutines.CoroutineScope
import retrofit2.http.Query
import javax.inject.Inject

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