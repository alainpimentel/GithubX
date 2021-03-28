package com.alainp.githubx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.alainp.githubx.databinding.FragmentUserDetailBinding
import com.alainp.githubx.viewmodels.UserDetailViewModel
import com.alainp.githubx.viewmodels.UserDetailViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        viewModel.userDetail.observe(viewLifecycleOwner) { userDetail ->
            binding.progressBar.isGone = true
            if (userDetail == null) {
                binding.errorText.isGone = false
            }
            userDetail?.let { userDets ->
                binding.codeInfoCardView.isGone = false
                binding.userInfoCardView.isGone = false

                binding.viewModel = viewModel
                if (userDets.company == null) {
                    binding.companyText.isGone = true
                } else {
                    binding.companyText.isGone = false
                    binding.companyText.text = userDets.company
                }

                if (userDets.location == null) {
                    binding.locationText.isGone = true
                } else {
                    binding.locationText.isGone = false
                    binding.locationText.text = userDets.location
                }

                binding.followersText.text =
                    String.format(getString(R.string.followers), userDets.followers)
                binding.followingText.text =
                    String.format(getString(R.string.following), userDets.publicGists)

                binding.publicReposValue.text = String.format("%,d", userDets.publicRepos)
                binding.publicGistValue.text = String.format("%,d", userDets.publicGists)
            }
        }
    }
}