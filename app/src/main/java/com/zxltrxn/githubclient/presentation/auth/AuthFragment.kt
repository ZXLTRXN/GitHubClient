package com.zxltrxn.githubclient.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.databinding.FragmentAuthBinding
import com.zxltrxn.githubclient.presentation.MainActivity
import com.zxltrxn.githubclient.presentation.auth.AuthViewModel.Action
import com.zxltrxn.githubclient.utils.collectLifecycleFlow
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
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentAuthBinding.inflate(inflater,container,false)
        val view = binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        (requireActivity() as MainActivity).supportActionBar?.hide()
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe(){
        collectLifecycleFlow(viewModel.actions){ action ->
            when (action){
                is Action.ShowError ->
                    Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
                is Action.RouteToMain -> navigateToRepositoriesList()
            }
        }
    }

    private fun navigateToRepositoriesList(){
        val action = AuthFragmentDirections
            .authFragmentToRepositoriesListFragment()
        this.findNavController().navigate(action)
    }
}
