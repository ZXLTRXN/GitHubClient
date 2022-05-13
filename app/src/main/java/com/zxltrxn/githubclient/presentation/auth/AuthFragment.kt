package com.zxltrxn.githubclient.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.databinding.FragmentAuthBinding
import com.zxltrxn.githubclient.presentation.MainActivity
import com.zxltrxn.githubclient.presentation.auth.AuthViewModel.Action
import com.zxltrxn.githubclient.presentation.auth.AuthViewModel.State
import com.zxltrxn.githubclient.utils.collectLatestLifecycleFlow
import com.zxltrxn.githubclient.utils.collectLifecycleFlow
import com.zxltrxn.githubclient.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            binding.inputLayout.error = if (state is State.InvalidInput) state.reason else null
            binding.progressCircular.visibility =
                if (state is State.Loading) View.VISIBLE else View.GONE
            binding.submitButton.isEnabled = state !is State.Loading
            binding.submitButton.setOnClickListener { viewModel.onSignButtonPressed() }
            binding.submitButton.text =
                if (state is State.Loading) "" else getString(R.string.btn_sign_in)
        }

        binding.inputEditText.doAfterTextChanged {
            viewModel.token.value = it.toString()
        }

        collectLifecycleFlow(viewModel.actions) { action ->
            when (action) {
                is Action.ShowError ->
                    showToast(action.message)
                is Action.RouteToMain -> navigateToRepositoriesList()
            }
        }
    }

    private fun navigateToRepositoriesList() {
        val action = AuthFragmentDirections
            .authFragmentToRepositoriesListFragment()
        this.findNavController().navigate(action)
    }


}
