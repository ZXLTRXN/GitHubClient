package com.zxltrxn.githubclient.presentation.repositoriesList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.databinding.FragmentRepositoriesListBinding
import com.zxltrxn.githubclient.presentation.MainActivity
import com.zxltrxn.githubclient.presentation.repositoriesList.RepositoriesListViewModel.State
import com.zxltrxn.githubclient.presentation.repositoriesList.recyclerView.RepositoriesAdapter
import com.zxltrxn.githubclient.utils.Constants.TAG
import com.zxltrxn.githubclient.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(R.layout.fragment_repositories_list) {
    private var _binding: FragmentRepositoriesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RepositoriesListViewModel>()
    private lateinit var adapter: RepositoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentRepositoriesListBinding.inflate(inflater,container,false)
        val view = binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            rvRepositoriesList.addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }

        (requireActivity() as MainActivity).supportActionBar?.run{
            show()
            title = getString(R.string.repositories_list_header)
        }

        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvRepositoriesList.adapter = null
        _binding = null
    }

    private fun setUpAdapter(){
        adapter = RepositoriesAdapter{ id ->
            navigateToDetailInfo(id)
        }
        binding.rvRepositoriesList.adapter = adapter
    }

    private fun observe(){
        collectLatestLifecycleFlow(viewModel.state){ state ->
            when(state){
                is State.Loaded -> {
                    adapter.submitList(state.repos)
                }
                is State.Error -> {
                    binding.tvRepositoriesError.text = state.error
                }
                is State.Empty -> {
                    binding.tvRepositoriesError.text = getString(R.string.empty_repositories_list)
                }
                else -> {}
            }
        }
    }

    private fun navigateToDetailInfo(repoId: Int){
        val action = RepositoriesListFragmentDirections
            .repositoriesListFragmentToDetailInfoFragment(repoId = repoId)
        this.findNavController().navigate(action)
    }
}