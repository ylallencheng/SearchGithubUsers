package com.ylallencheng.searchgithubusers.ui.search

import android.os.Bundle
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

/**
 * Activity served as View in MVVM pattern
 */
class SearchActivity : DaggerAppCompatActivity() {

    /*
        ViewModelFactory and ViewModel
     */
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: SearchViewModel by viewModels { viewModelFactory }

    /*
        View Binding
     */
    private lateinit var binding: ActivitySearchBinding

    /* ------------------------------ Overrides */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
            inflate content view with view binding
         */
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
            init TextInputLayout and RecyclerView
         */
        initTextInputLayout(binding)
        initRecyclerView(binding)

        /*
            start observing livedatas in view model
         */
        observe()
    }

    /* ------------------------------ UI */

    /**
     * Initialize TextInputLayout.
     *
     * @param binding The view binding of the activity
     */
    private fun initTextInputLayout(binding: ActivitySearchBinding) {

        /*
            add listener to monitor editor action on keyboard
         */
        binding.textInputEditText.setOnEditorActionListener { v, actionId, _ ->

            /*
                a nested function to close keyboard and clear focus whenever there's an action triggered
             */
            fun finishEditing(activity: AppCompatActivity) {
                val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                val currentFocusedView = currentFocus ?: View(activity)
                imm.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
                currentFocusedView.clearFocus()
            }

            when (actionId) {

                /*
                    search action,
                    ask view model to search users with given query
                 */
                EditorInfo.IME_ACTION_SEARCH -> viewModel.searchGithubUsers(v.text.toString())
            }

            /*
                finish editing
             */
            finishEditing(this)

            /*
                return true when the action has been handled
             */
            true
        }
    }

    /**
     * Initialize RecyclerView.
     *
     * @param binding The view binding of the activity
     */
    private fun initRecyclerView(binding: ActivitySearchBinding) {
        /*
            set up layout manager
         */
        binding.recyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        /*
            generate adapter
         */
        binding.recyclerView.adapter = UserAdapter()
    }

    /* ------------------------------ Observe */

    /**
     * Observe livedata in view model.
     *
     * Use lifecycleScope.launchWhenResume{} to ensure:
     *
     *  1. the job lives as long as the lifecycle owner (activity) lives
     *  2. the job will be active only when lifecycle owner is in at least resume state
     */
    private fun observe() = lifecycleScope.launchWhenResumed {

        /*
            Observe request status
         */
        viewModel.requestStatus.observe(this@SearchActivity) {
            when (it.status) {

                /*
                    loading or refreshing, show the progress bar
                 */
                Status.LOADING, Status.REFRESHING -> binding.contentLoadingProgressBar.visibility = View.VISIBLE

                /*
                    request has succeeded, hide the progress bar
                 */
                Status.SUCCESS -> binding.contentLoadingProgressBar.visibility = View.GONE

                /*
                    request has failed, hide the progress bar and show error message
                 */
                Status.FAILED -> {
                    Toast.makeText(this@SearchActivity, it.errorMessage, Toast.LENGTH_SHORT).show()
                    binding.contentLoadingProgressBar.visibility = View.GONE
                }
            }
        }

        /*
            Observe paged user list
         */
        viewModel.users.observe(this@SearchActivity) {
            /*
                submit paged user list to adapter
             */
            (binding.recyclerView.adapter as UserAdapter).submitList(it)
        }
    }
}
