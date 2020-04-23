package com.ylallencheng.searchgithubusers.io.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.squareup.moshi.Moshi
import com.ylallencheng.searchgithubusers.io.GithubService
import com.ylallencheng.searchgithubusers.io.SearchUsersRs
import com.ylallencheng.searchgithubusers.io.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Exception
import javax.inject.Inject

class GithubUserDataSource @Inject constructor(
        private val service: GithubService) : PageKeyedDataSource<Int, User>() {

    companion object {
        const val TAG = "GithubUserDataSource"
    }

    var scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    var query: String = ""

    override fun loadInitial(params: LoadInitialParams<Int>,
                             callback: LoadInitialCallback<Int, User>) {
        scope.launch {
            try {
                val rs = service.searchUsers(
                        query = query,
                        page = 1,
                        pageLimit = params.requestedLoadSize
                )
                callback.onResult(rs.items, null, 2)
            } catch (e: HttpException) {
//                e.response().errorBody().
//                val converter = Moshi..responseBodyConverter().convert()
                Log.d(TAG, "error message: ${e.message}")
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>,
                           callback: LoadCallback<Int, User>) {

        scope.launch {
            try {
                val rs = service.searchUsers(
                        query = query,
                        page = params.key,
                        pageLimit = params.requestedLoadSize
                )
                callback.onResult(rs.items, params.key + 1)
            } catch (e: Throwable) {
                Log.d(TAG, "error message: ${e.message}")
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>,
                            callback: LoadCallback<Int, User>) {
    }
}