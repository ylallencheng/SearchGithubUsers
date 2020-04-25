package com.ylallencheng.searchgithubusers.io.pagination

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.squareup.moshi.Moshi
import com.ylallencheng.searchgithubusers.io.model.ErrorRs
import com.ylallencheng.searchgithubusers.io.model.User
import com.ylallencheng.searchgithubusers.io.service.GithubService
import com.ylallencheng.searchgithubusers.io.util.ApiUtil
import com.ylallencheng.searchgithubusers.io.util.RequestStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * The data source object used for pagination.
 *
 * @property mService The service to request api
 * @property mContext The App context to check network availability
 * @property mScope The scope where the jobs need to be executed
 * @property mQuery The query of the request
 */
class GithubUserDataSource(private val mService: GithubService,
                           private val mContext: Context,
                           private val mScope: CoroutineScope,
                           private val mQuery: String) : PageKeyedDataSource<Int, User>() {

    /*
        RequestStatus LiveData
     */
    val requestStatus: MutableLiveData<RequestStatus> = MutableLiveData()

    /* ------------------------------ Overrides */

    override fun loadInitial(params: LoadInitialParams<Int>,
                             callback: LoadInitialCallback<Int, User>) {
        load(
                page = 1,
                pageLimit = params.requestedLoadSize) { users, nextPage ->
            callback.onResult(users, null, nextPage)
        }
    }

    override fun loadAfter(params: LoadParams<Int>,
                           callback: LoadCallback<Int, User>) {
        load(
                page = params.key,
                pageLimit = params.requestedLoadSize) { users, nextPage ->
            callback.onResult(users, nextPage)
        }
    }

    override fun loadBefore(params: LoadParams<Int>,
                            callback: LoadCallback<Int, User>) {
        // we don't need this
    }

    /* ------------------------------ Private Methods */

    /**
     * Base method of loading data from service.
     *
     * @param page Indicate which page should be loaded
     * @param pageLimit Indicate the size of the page
     * @param callback A callback to trigger [LoadInitialCallback] or [LoadCallback]
     */
    private fun load(page: Int,
                     pageLimit: Int,
                     callback: (List<User>, Int) -> Unit) =
            /*
                launch the scope with IO dispatcher to assure it's running on IO thread
             */
            mScope.launch(Dispatchers.IO) {

                /*
                    check network availability, return failed status if there's no network
                */
                if (!ApiUtil.isNetworkAvailable(mContext)) {
                    requestStatus.postValue(RequestStatus.failed("Network unavailable"))
                    return@launch
                }

                /*
                    use page to determine whether it's loading or refreshing
                 */
                when (page) {
                    1 -> requestStatus.postValue(RequestStatus.loading())
                    else -> requestStatus.postValue(RequestStatus.refreshing())
                }

                /*
                    start fetching users via service
                 */
                try {
                    val rs = mService.searchUsers(
                            query = mQuery,
                            page = page,
                            pageLimit = pageLimit)

                    /*
                        callback with the users and next page when it's successful
                     */
                    callback(rs.items, page + 1)

                    /*
                        update the request status to success
                     */
                    requestStatus.postValue(RequestStatus.success())

                } catch (e: HttpException) {

                    /*
                        http exception occurred, convert the exception to extract error message
                     */
                    val errorRs = convertErrorRs(e)

                    /*
                        update the request status to failed with error message
                     */
                    requestStatus.postValue(
                            RequestStatus.failed(
                                    errorRs?.message
                                            ?: e.message
                                            ?: "Something went wrong"))

                } catch (e: SocketTimeoutException) {
                    /*
                        timed out exception, update the request status with error message
                     */
                    requestStatus.postValue(
                            RequestStatus.failed("Operation has Timed-out"))

                } catch (e: Exception) {
                    /*
                        some other exception occurred, update the request status with error message
                     */
                    requestStatus.postValue(
                            RequestStatus.failed(e.message ?: "Something went wrong"))
                }
            }


    /**
     * Converting HttpException to ErrorRs to get errorMessage
     */
    private fun convertErrorRs(e: HttpException): ErrorRs? =
            try {
                /*
                    get the error buffered source and parse to ErrorRs
                 */
                e.response()?.errorBody()?.source()?.let {
                    val moshiAdapter = Moshi.Builder().build().adapter(ErrorRs::class.java)
                    moshiAdapter.fromJson(it)
                }

            } catch (e: IOException) {
                // something went wrong, just return null
                null
            }
}