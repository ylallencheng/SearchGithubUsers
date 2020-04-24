package com.ylallencheng.searchgithubusers.ui.search

import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ylallencheng.searchgithubusers.io.model.User

class UserAdapter : PagedListAdapter<User, UserAdapter.UserViewHolder>(User.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): UserViewHolder {
        return UserViewHolder(TextView(parent.context))
    }

    override fun onBindViewHolder(holder: UserViewHolder,
                                  position: Int) {
        holder.rootView.text = getItem(position)?.username ?: "username"
    }

    class UserViewHolder(val rootView: TextView) : RecyclerView.ViewHolder(rootView) {
    }
}
