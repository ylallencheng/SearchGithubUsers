package com.ylallencheng.searchgithubusers.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.ylallencheng.searchgithubusers.databinding.ItemUserBinding
import com.ylallencheng.searchgithubusers.io.model.User

class UserAdapter : PagedListAdapter<User, UserAdapter.UserViewHolder>(User.DIFF_CALLBACK) {

    companion object {
        const val VIEW_TYPE_1_ON_1 = 0
        const val VIEW_TYPE_2_ON_1 = 1
        const val VIEW_TYPE_2_ON_2 = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (viewType == VIEW_TYPE_2_ON_1 || viewType == VIEW_TYPE_2_ON_2) {
            (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
        }
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder,
                                  position: Int) {
        getItem(position)?.also { holder.bind(it) }
    }

    override fun getItemViewType(position: Int): Int =
            when (getItem(position)?.id?.rem(3)) {
                1 -> VIEW_TYPE_2_ON_1
                2 -> VIEW_TYPE_2_ON_2
                else -> VIEW_TYPE_1_ON_1
            }

    class UserViewHolder(private val mBinding: ItemUserBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(user: User) {
            mBinding.textViewUsername.text = user.username
            Glide.with(mBinding.root).load(user.avatarUrl).into(mBinding.imageViewAvatar)
        }
    }
}
