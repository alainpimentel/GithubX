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

class UserListAdapter :
    PagingDataAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UserViewHolder(
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
            (holder as UserViewHolder).bind(restaurant, position)
        }
    }

    class UserViewHolder(
        private val binding: ListItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener { view ->
                view.findNavController()
                    .navigate(UserListFragmentDirections.actionUserListFragmentToUserDetailFragment())
            }
        }

        fun bind(item: User, position: Int) {
            //todo remove position
            binding.userNameText.text = "$position - ${item.login}"
        }

    }
}


private class UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}
