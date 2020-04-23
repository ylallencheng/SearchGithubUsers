package com.ylallencheng.searchgithubusers.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
        private val mViewModelMap: Map<Class<out ViewModel>,
                @JvmSuppressWildcards Provider<ViewModel>>) : ViewModelProvider.Factory {

    /* ------------------------------ Override  */

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var viewModel = mViewModelMap[modelClass]

        // check the null view model is a superclass or superinterface
        if (viewModel == null) {
            for ((key, value) in mViewModelMap) {
                if (modelClass.isAssignableFrom(key)) {
                    viewModel = value
                    break
                }
            }
        }

        // if view model is not in view model map or
        // not a superclass or superinterface of the view model, throw exception
        requireNotNull(viewModel) { "unknown model class $modelClass" }

        // return view model
        try {
            return viewModel.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}