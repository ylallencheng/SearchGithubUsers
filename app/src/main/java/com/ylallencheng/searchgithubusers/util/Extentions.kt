package com.ylallencheng.searchgithubusers.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * A live data extension to simplify observe boilerplate
 */
fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner,
                            onChange: (T) -> Unit) {
    observe(
            lifecycleOwner,
            Observer {
                onChange(it)
            })
}