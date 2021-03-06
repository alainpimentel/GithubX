package com.alainp.githubx

import GridAutofitLayoutManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.alainp.githubx.adapters.UserListAdapter
import com.alainp.githubx.adapters.UsersLoadStateAdapter
import com.alainp.githubx.databinding.FragmentUserListBinding
import com.alainp.githubx.viewmodels.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private val viewModel: UserListViewModel by viewModels()
    private lateinit var binding: FragmentUserListBinding
    private lateinit var adapter: UserListAdapter
    private var isFirstLoad = true

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
        adapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = !isFirstLoad && loadState.refresh is LoadState.Loading && adapter.itemCount == 0
            binding.errorText.isVisible = loadState.refresh is LoadState.Error && adapter.itemCount == 0
            isFirstLoad = false
        }
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