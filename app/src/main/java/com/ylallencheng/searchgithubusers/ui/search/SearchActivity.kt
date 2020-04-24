package com.ylallencheng.searchgithubusers.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ylallencheng.searchgithubusers.databinding.ActivitySearchBinding
import com.ylallencheng.searchgithubusers.di.viewModel.ViewModelFactory
import com.ylallencheng.searchgithubusers.io.model.RequestStatus
import com.ylallencheng.searchgithubusers.io.model.Status
import com.ylallencheng.searchgithubusers.util.observe
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SearchActivity : DaggerAppCompatActivity() {

    companion object {
        const val TAG = "SearchActivity"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: SearchViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textInputEditText.setOnEditorActionListener { v, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val searchQuery = v.text.toString()
                    Log.d(TAG, "search query is: $searchQuery")
                    viewModel.searchUsers(searchQuery)
                }
            }
            finishEditing(this)
            true
        }

        binding.recyclerView.adapter = UserAdapter()
    }

    override fun onResume() {
        super.onResume()

        viewModel.requestStatus.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    Log.d(TAG, "Loading")
                    binding.contentLoadingProgressBar.visibility = View.VISIBLE
                }
                Status.FAILED -> {
                    Log.d(TAG, "Failed with message: ${it.errorMessage}")
                    binding.contentLoadingProgressBar.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "Succeeded")
                    binding.contentLoadingProgressBar.visibility = View.GONE
                }
                Status.REFRESHING -> {
                    Log.d(TAG, "Refreshing")
                    binding.contentLoadingProgressBar.visibility = View.VISIBLE
                }
            }
        }

        viewModel.users.observe(this) {
            (binding.recyclerView.adapter as UserAdapter).submitList(it)
        }
    }

    private fun finishEditing(activity: AppCompatActivity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
        currentFocusedView.clearFocus()
    }
}
