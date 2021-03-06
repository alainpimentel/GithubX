package com.alainp.githubx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alainp.githubx.UserListFragmentDirections
import com.alainp.githubx.data.User
import com.alainp.githubx.databinding.ListItemUserBinding
import com.alainp.githubx.viewmodels.ListItemUserViewModel

class UserListAdapter :
    PagingDataAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListUserViewHolder(
            ListItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val restaurant = getItem(position)
        if (restaurant != null) {
            (holder as ListUserViewHolder).bind(restaurant, position)
        }
    }


    class ListUserViewHolder(
        private val binding: ListItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var user: User? = null

        init {
            binding.setClickListener { view ->
                user?.let {
                    view.findNavController()
                        .navigate(
                            UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(
                                it.login
                            )
                        )
                }
            }
        }

        fun bind(item: User, position: Int) {
            user = item
            binding.apply {
                viewmodel = ListItemUserViewModel(item)
                executePendingBindings()
            }
        }

    }
}


private class UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}
