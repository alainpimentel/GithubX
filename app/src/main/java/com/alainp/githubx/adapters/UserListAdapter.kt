package com.alainp.githubx.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alainp.githubx.UserListFragmentDirections
import com.alainp.githubx.data.User
import com.alainp.githubx.databinding.GridItemUserBinding
import com.alainp.githubx.databinding.ListItemUserBinding
import com.alainp.githubx.viewmodels.ListItemUserViewModel

class UserListAdapter :
    PagingDataAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback()) {
    var viewType = ViewType.LIST
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (this.viewType == ViewType.LIST) {
            ListUserViewHolder(
                ListItemUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            GridUserViewHolder(
                GridItemUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val restaurant = getItem(position)
        if (restaurant != null) {
            (holder as UserViewHolder).bind(restaurant, position)
        }
    }

    abstract class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: User, position: Int)
    }

    class ListUserViewHolder(
        private val binding: ListItemUserBinding
    ) : UserViewHolder(binding.root) {

        init {
            binding.setClickListener { view ->
                view.findNavController()
                    .navigate(UserListFragmentDirections.actionUserListFragmentToUserDetailFragment())
            }
        }

        override fun bind(item: User, position: Int) {
            binding.apply {
                viewmodel = ListItemUserViewModel(item)
                executePendingBindings()
            }
            binding.userNameText.text = "$position - ${item.login}"
        }

    }

    class GridUserViewHolder(
        private val binding: GridItemUserBinding
    ) : UserViewHolder(binding.root) {

        init {
            binding.setClickListener { view ->
                view.findNavController()
                    .navigate(UserListFragmentDirections.actionUserListFragmentToUserDetailFragment())
            }
        }

        override fun bind(item: User, position: Int) {
            binding.apply {
                viewmodel = ListItemUserViewModel(item)
                executePendingBindings()
            }
            binding.userNameText.text = "$position - ${item.login}"
        }

    }

    enum class ViewType(val viewType: Int) {
        LIST(ITEM_VIEW_TYPE_GRID),
        GRID(ITEM_VIEW_TYPE_LIST)
    }

    companion object {
        private const val ITEM_VIEW_TYPE_LIST = 0
        private const val ITEM_VIEW_TYPE_GRID = 1
    }
}


private class UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}
