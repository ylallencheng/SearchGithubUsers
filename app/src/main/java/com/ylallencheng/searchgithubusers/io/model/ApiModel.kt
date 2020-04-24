package com.ylallencheng.searchgithubusers.io.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class SearchUsersRs(@field:Json(name = "total_count") val totalCount: Int,
                         @field:Json(name = "incomplete_results") val incompleteResults: Boolean,
                         val items: List<User>)

data class User(@field:Json(name = "login") val username: String?,
                @field:Json(name = "avatar_url") val avatarUrl: String?) {
    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User,
                                         newItem: User): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: User,
                                            newItem: User): Boolean =
                    oldItem.username.equals(newItem.username, ignoreCase = true)
        }
    }
}

data class ErrorRs(val message: String?,
                   val errors: List<ErrorBody>?)

data class ErrorBody(val resource: String?,
                     val field: String?,
                     val code: String?)