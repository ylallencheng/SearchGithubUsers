package com.ylallencheng.searchgithubusers.io.paging

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.squareup.moshi.Moshi
import com.ylallencheng.searchgithubusers.io.GithubService
import com.ylallencheng.searchgithubusers.io.model.ErrorRs
import com.ylallencheng.searchgithubusers.io.model.RequestStatus
import com.ylallencheng.searchgithubusers.io.model.Status
import com.ylallencheng.searchgithubusers.io.model.User
import com.ylallencheng.searchgithubusers.io.util.ApiUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class GithubUserDataSource(private val mService: GithubService,
                           private val mContext: Context,
                           private val mScope: CoroutineScope,
                           private val mQuery: String) : PageKeyedDataSource<Int, User>() {

    companion object {
        const val TAG = "GithubUserDataSource"
    }

    val requestStatus: MutableLiveData<RequestStatus> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>,
                             callback: LoadInitialCallback<Int, User>) {
        mScope.launch(Dispatchers.IO) {

            if (!ApiUtil.isNetworkAvailable(mContext)) {
                // return error
                requestStatus.postValue(RequestStatus(status = Status.FAILED, errorMessage = "Network not available"))
                return@launch
            }

            requestStatus.postValue(RequestStatus(status = Status.LOADING))

            try {
                val rs = mService.searchUsers(
                        query = mQuery,
                        page = 1,
                        pageLimit = params.requestedLoadSize)
                callback.onResult(rs.items, null, 2)
                requestStatus.postValue(RequestStatus(status = Status.SUCCESS))
            } catch (e: HttpException) {
                val body = convertErrorRs(e)
                requestStatus.postValue(
                        RequestStatus(
                                status = Status.FAILED,
                                errorMessage = body?.message ?: e.message))
            } catch (e: Exception) {
                requestStatus.postValue(
                        RequestStatus(
                                status = Status.FAILED,
                                errorMessage = e.message))
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>,
                           callback: LoadCallback<Int, User>) {
        mScope.launch(Dispatchers.IO) {

            if (!ApiUtil.isNetworkAvailable(mContext)) {
                // return error
                requestStatus.postValue(RequestStatus(status = Status.FAILED, errorMessage = "Network not available"))
                return@launch
            }

            requestStatus.postValue(RequestStatus(status = Status.REFRESHING))

            try {
                val rs = mService.searchUsers(
                        query = mQuery,
                        page = params.key,
                        pageLimit = params.requestedLoadSize)
                callback.onResult(rs.items, params.key + 1)
                requestStatus.postValue(RequestStatus(status = Status.SUCCESS))
            } catch (e: HttpException) {
                val body = convertErrorRs(e)
                requestStatus.postValue(
                        RequestStatus(
                                status = Status.FAILED,
                                errorMessage = body?.message ?: e.message))
            } catch (e: Exception) {
                requestStatus.postValue(
                        RequestStatus(
                                status = Status.FAILED,
                                errorMessage = e.message))
            }

        }
    }

    override fun loadBefore(params: LoadParams<Int>,
                            callback: LoadCallback<Int, User>) {
    }

    private fun convertErrorRs(e: HttpException): ErrorRs? =
            try {
                e.response()?.errorBody()?.source()?.let {
                    val moshiAdapter = Moshi.Builder().build().adapter(ErrorRs::class.java)
                    moshiAdapter.fromJson(it)
                }
            } catch (e: IOException) {
                null
            }
}