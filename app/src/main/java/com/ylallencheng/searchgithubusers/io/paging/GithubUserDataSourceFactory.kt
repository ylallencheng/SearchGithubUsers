package com.ylallencheng.searchgithubusers.io.paging

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ylallencheng.searchgithubusers.io.service.GithubService
import com.ylallencheng.searchgithubusers.io.model.User
import kotlinx.coroutines.CoroutineScope

/**
 * The factory to generate [GithubUserDataSource] for pagination.
 *
 * @property mService The service to request api
 * @property mContext The App context to check network availability
 * @property mScope The scope where the jobs need to be executed
 * @property mQuery The query of the request
 */
class GithubUserDataSourceFactory(private val mService: GithubService,
                                  private val mContext: Context,
                                  private val mScope: CoroutineScope,
                                  private val mQuery: String) : DataSource.Factory<Int, User>() {
    /*
        DataSource LiveData
     */
    val dataSourceLiveData = MutableLiveData<GithubUserDataSource>()

    /* ------------------------------ Overrides */

    override fun create(): DataSource<Int, User> {

        /*
            generate the data source
         */
        val dataSource = GithubUserDataSource(mService, mContext, mScope, mQuery)

        /*
            update the live data with data source
         */
        dataSourceLiveData.postValue(dataSource)

        return dataSource
    }
}