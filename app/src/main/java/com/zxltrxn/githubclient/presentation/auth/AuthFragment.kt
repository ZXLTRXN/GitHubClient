package com.zxltrxn.githubclient.presentation.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.databinding.FragmentAuthBinding
import com.zxltrxn.githubclient.presentation.auth.AuthViewModel.State
import com.zxltrxn.githubclient.presentation.auth.AuthViewModel.Action
import com.zxltrxn.githubclient.utils.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        collectLatestLifecycleFlow(viewModel.state){ state ->
            when (state){
                is State.Loading -> setLoadingState()
                is State.InvalidInput -> setInputError(state.reason)
                is State.Idle -> setDefaultState()
            }
        }
        collectLifecycleFlow(viewModel.actions){ action ->
            when (action){
                is Action.ShowError -> showToast(action.message)
                is Action.RouteToMain -> navigateToRepositoriesList()
            }
        }
    }

    private fun navigateToRepositoriesList(){
//        findNavController().navigate(FragmentAuth)
        Log.d(TAG, "navigateToRepositoriesList")
    }

    private fun setLoadingState(){
        Log.d(TAG, "setLoadingState")
    }

    private fun setDefaultState(){
        Log.d(TAG, "setDefaultState")
    }

    private fun setInputError(reason: String){
        Log.d(TAG, "setInputError: $reason")
    }

    private fun showToast(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T)->Unit){
    viewLifecycleOwner.lifecycleScope.launch{
        repeatOnLifecycle(Lifecycle.State.STARTED){
            flow.collectLatest(collect)
        }
    }
}

fun <T> Fragment.collectLifecycleFlow(flow: Flow<T>, collect: FlowCollector<T>){
    viewLifecycleOwner.lifecycleScope.launch{
        repeatOnLifecycle(Lifecycle.State.STARTED){
            flow.collect(collect)
        }
    }
}