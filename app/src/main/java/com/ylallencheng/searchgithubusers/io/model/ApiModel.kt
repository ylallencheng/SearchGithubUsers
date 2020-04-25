package com.ylallencheng.searchgithubusers.io.model

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import kotlin.math.abs
import kotlin.random.Random

/**
 * The response data model of successful user search request to Github API v3
 */
data class SearchUsersRs(@field:Json(name = "total_count") val totalCount: Int,
                         @field:Json(name = "incomplete_results") val incompleteResults: Boolean,
                         val items: List<User>)

/**
 * The data model of searched Github users.
 * Only required fields are collected into the model.
 */
data class User(val id: Int,
                @field:Json(name = "login") val username: String?,
                @field:Json(name = "avatar_url") val avatarUrl: String?,
                val randomId: Int?) {

    companion object {

        /*
            the callback required for PagedListAdapter where it's used for comparing users in the list
         */
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User,
                                         newItem: User): Boolean =
                    oldItem == newItem

            override fun areContentsTheSame(oldItem: User,
                                            newItem: User): Boolean =
                    oldItem.id == newItem.id
        }
    }
}

/**
 * The response data model of issued user search request to Github API v3
 */
data class ErrorRs(val message: String?,
                   val errors: List<Error>?)

/**
 * The data model of error listed in [ErrorRs]
 */
data class Error(val resource: String?,
                 val field: String?,
                 val code: String?)