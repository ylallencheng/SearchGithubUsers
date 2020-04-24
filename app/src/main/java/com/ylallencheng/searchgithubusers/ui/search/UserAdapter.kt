package com.ylallencheng.searchgithubusers.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ylallencheng.searchgithubusers.databinding.ItemUserBinding
import com.ylallencheng.searchgithubusers.io.model.User

class UserAdapter : PagedListAdapter<User, UserAdapter.UserViewHolder>(User.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder,
                                  position: Int) {
        getItem(position)?.also { holder.bind(it) }
    }

    class UserViewHolder(private val mBinding: ItemUserBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(user: User) {
            mBinding.textViewUsername.text = user.username
        }
    }
}
