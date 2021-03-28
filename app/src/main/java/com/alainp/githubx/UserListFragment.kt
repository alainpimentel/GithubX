package com.alainp.githubx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.alainp.githubx.adapters.UserListAdapter
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
        binding.userList.layoutManager = GridLayoutManager(context, 2)
        binding.userList.adapter = adapter
        adapter.viewType = UserListAdapter.ViewType.GRID
        subscribeUI()

        setHasOptionsMenu(false)

//        val service = GithubService.create()
//        lifecycleScope.launchWhenCreated {
//            val users = service.getUsers(1)
//            Log.d("messi", "$users")
//        }

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }

    private fun subscribeUI() {
        lifecycleScope.launchWhenCreated {
            viewModel.users.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}