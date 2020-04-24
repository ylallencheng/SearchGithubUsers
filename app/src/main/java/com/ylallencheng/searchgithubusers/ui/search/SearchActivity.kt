package com.ylallencheng.searchgithubusers.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ylallencheng.searchgithubusers.databinding.ActivitySearchBinding
import com.ylallencheng.searchgithubusers.di.viewModel.ViewModelFactory
import com.ylallencheng.searchgithubusers.io.util.Status
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

        initTextInputLayout(binding)
        initRecyclerView(binding)

        observe()
    }

    private fun initTextInputLayout(binding: ActivitySearchBinding) {

        binding.textInputEditText.setOnEditorActionListener { v, actionId, _ ->

            fun finishEditing(activity: AppCompatActivity) {
                val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                val currentFocusedView = currentFocus ?: View(activity)
                imm.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
                currentFocusedView.clearFocus()
            }

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
    }

    private fun initRecyclerView(binding: ActivitySearchBinding) {
        val adapter = UserAdapter()
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

    private fun observe() = lifecycleScope.launchWhenResumed {
        viewModel.requestStatus.observe(this@SearchActivity) {
            when (it.status) {
                Status.LOADING -> binding.contentLoadingProgressBar.visibility = View.VISIBLE
                Status.SUCCESS -> binding.contentLoadingProgressBar.visibility = View.GONE
                Status.REFRESHING -> binding.contentLoadingProgressBar.visibility = View.VISIBLE
                Status.FAILED -> {
                    Toast.makeText(this@SearchActivity, it.errorMessage, Toast.LENGTH_SHORT).show()
                    binding.contentLoadingProgressBar.visibility = View.GONE
                }
            }
        }

        viewModel.users.observe(this@SearchActivity) {
            (binding.recyclerView.adapter as UserAdapter).submitList(it)
        }
    }
}
