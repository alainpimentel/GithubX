package com.alainp.githubx

import GridAutofitLayoutManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alainp.githubx.adapters.UserListAdapter
import com.alainp.githubx.adapters.UsersLoadStateAdapter
import com.alainp.githubx.databinding.FragmentUserListBinding
import com.alainp.githubx.viewmodels.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class UserListFragment : Fragment() {

    private val viewModel: UserListViewModel by viewModels()
    private lateinit var binding: FragmentUserListBinding
    private lateinit var adapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)

        adapter = UserListAdapter()
        setRecyclerViewLayoutManager()
        binding.userList.adapter = adapter.withLoadStateFooter(
            footer = UsersLoadStateAdapter(adapter)
        )
        subscribeUI()

        setHasOptionsMenu(false)

        binding.fab.setOnClickListener {
            viewModel.toggleViewType()
            setRecyclerViewLayoutManager()
        }

        return binding.root
    }


    private fun setRecyclerViewLayoutManager() {
        context?.let {
            var scrollPosition = 0

            if (binding.userList.layoutManager != null) {
                scrollPosition = (binding.userList.layoutManager as LinearLayoutManager)
                    .findFirstCompletelyVisibleItemPosition()
            }
            if (viewModel.viewType == UserListViewModel.ViewType.LIST) {
                binding.userList.layoutManager = LinearLayoutManager(context)
                UserListViewModel.ViewType.LIST
            } else {
                // TODO change span count based on device
                val colWidth =
                    it.resources.getDimension(R.dimen.grid_column_width).toInt()
                binding.userList.layoutManager = GridAutofitLayoutManager(it, colWidth)
                UserListViewModel.ViewType.GRID
            }

            binding.userList.scrollToPosition(scrollPosition)
        }
    }

    private fun subscribeUI() {
        lifecycleScope.launchWhenCreated {
            viewModel.users.collectLatest {
                adapter.submitData(it)
            }
        }
    }

}