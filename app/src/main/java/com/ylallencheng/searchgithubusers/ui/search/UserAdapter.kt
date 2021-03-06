package com.ylallencheng.searchgithubusers.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.ylallencheng.searchgithubusers.R
import com.ylallencheng.searchgithubusers.databinding.ItemUserBinding
import com.ylallencheng.searchgithubusers.io.model.User

/**
 * PagedListAdapter required for RecyclerView pagination
 */
class UserAdapter : PagedListAdapter<User, UserAdapter.UserViewHolder>(User.DIFF_CALLBACK) {

    companion object {

        // View Types
        const val VIEW_TYPE_1_ON_1 = 0
        const val VIEW_TYPE_2_ON_1 = 1
        const val VIEW_TYPE_2_ON_2 = 2
    }

    /* ------------------------------ Overrides */

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): UserViewHolder {
        /*
            inflate view by view binding
         */
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        /*
            determine corresponding action based on the view type.

            for row spanning requirement, set fullSpan to layout params
            for column spanning requirement, set height based on item view's width after layout
         */
        when (viewType) {
            VIEW_TYPE_1_ON_1 -> {
                binding.root.viewTreeObserver.addOnGlobalLayoutListener(
                        object : ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                binding.root.layoutParams.height = binding.root.width
                                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            }
                        })
            }

            VIEW_TYPE_2_ON_1 -> {
                (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                binding.root.viewTreeObserver.addOnGlobalLayoutListener(
                        object : ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                binding.root.layoutParams.height = binding.root.width / 2
                                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            }
                        })
            }

            VIEW_TYPE_2_ON_2 -> {
                (binding.root.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                binding.root.viewTreeObserver.addOnGlobalLayoutListener(
                        object : ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                binding.root.layoutParams.height = binding.root.width
                                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            }
                        })
            }
        }

        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder,
                                  position: Int) {
        getItem(position)?.also { holder.bind(it) }
    }

    override fun getItemViewType(position: Int): Int =
            when (getItem(position)?.randomId) {
                1 -> VIEW_TYPE_2_ON_1
                2 -> VIEW_TYPE_2_ON_2
                else -> VIEW_TYPE_1_ON_1
            }

    /* ------------------------------ View Holder */

    /**
     * View holder class used for displaying items in recycler view
     */
    class UserViewHolder(private val mBinding: ItemUserBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bind(user: User) {
            mBinding.textViewUsername.text = user.username
            Glide
                    .with(mBinding.root)
                    .load(user.avatarUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(mBinding.imageViewAvatar)
        }
    }
}
