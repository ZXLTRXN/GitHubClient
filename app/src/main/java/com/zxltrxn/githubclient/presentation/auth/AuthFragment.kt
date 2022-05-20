package com.zxltrxn.githubclient.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.databinding.FragmentAuthBinding
import com.zxltrxn.githubclient.presentation.auth.AuthScreenViewModel.Action
import com.zxltrxn.githubclient.presentation.auth.AuthScreenViewModel.State
import com.zxltrxn.githubclient.utils.bindTextTwoWay
import com.zxltrxn.githubclient.utils.collectActions
import com.zxltrxn.githubclient.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthScreenViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        setHasOptionsMenu(false)
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

    private fun setUpViews(state: State) {
        with(binding) {
            inputLayout.error =
                if (state is State.InvalidInput) state.reason.getString(requireContext()) else null
            progressCircular.visibility =
                if (state is State.Loading) View.VISIBLE else View.GONE
            submitButton.isEnabled = state !is State.Loading
            submitButton.setOnClickListener { viewModel.onSignButtonPressed() }
            submitButton.text =
                if (state is State.Loading) "" else getString(R.string.btn_sign_in)
        }
    }

    private fun observe() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            setUpViews(state)
        }

        binding.inputEditText.bindTextTwoWay(
            stateFlow = viewModel.token,
            lifecycleOwner = viewLifecycleOwner
        )

        collectActions(viewModel.actions) { action ->
            when (action) {
                is Action.ShowError ->
                    ErrorDialogFragment(action.message, action.code).show(
                        childFragmentManager, ErrorDialogFragment.TAG
                    )
                is Action.RouteToMain -> navigateToRepositoriesList()
            }
        }
    }

    private fun navigateToRepositoriesList() {
        val action = AuthFragmentDirections
            .toRepositoriesListFragment()
        this.findNavController().navigate(action)
    }


}
