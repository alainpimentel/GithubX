package com.alainp.githubx

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.alainp.githubx.databinding.FragmentUserDetailBinding
import com.alainp.githubx.viewmodels.UserDetailViewModel
import com.alainp.githubx.viewmodels.UserDetailViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private val args: UserDetailFragmentArgs by navArgs()
    @Inject
    lateinit var userDetailViewModelViewModelFactory: UserDetailViewModelFactory
    private val viewModel: UserDetailViewModel by viewModels() {
        UserDetailViewModel.provideFactory(
            userDetailViewModelViewModelFactory,
            args.username
        )
    }
    private lateinit var binding: FragmentUserDetailBinding


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_detail,
            container,
            false
        )
        subscribeUI()
        return binding.root
    }

    private fun subscribeUI() {
//        binding.viewModel = viewModel
//        binding.executePendingBindings()

        viewModel.userDetail.observe(viewLifecycleOwner) { userDetail ->
            Log.d("messi", "The user is here $userDetail")
            binding.viewModel = viewModel
            //binding.userDetailLogin.text = userDetail.login
        }
    }
}