package com.ylallencheng.searchgithubusers.io.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import javax.inject.Inject

object ApiUtil {

    @Suppress("DEPRECATION")
    fun isNetworkAvailable(context: Context): Boolean {

        val connectivityManager =
                ContextCompat.getSystemService(context, ConnectivityManager::class.java)
                        ?: return false

        when {
            // Android 8 or above
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val network =
                        connectivityManager.activeNetwork ?: return false
                val networkCapabilities =
                        connectivityManager.getNetworkCapabilities(network) ?: return false
                return when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }

            // previous than Android 8
            else ->
                return connectivityManager.activeNetworkInfo?.isConnected == true
        }
    }
}