package com.ylallencheng.searchgithubusers.ui.search

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.ylallencheng.searchgithubusers.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SearchActivity"
    }

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textInputEditText.setOnEditorActionListener { v, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val searchQuery = v.text
                    Log.d(TAG, "search query is: $searchQuery")
                }
            }
            finishEditing(this)
            true
        }
    }

    private fun finishEditing(activity: AppCompatActivity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(currentFocusedView.windowToken, 0)
        currentFocusedView.clearFocus()
    }
}
