package com.zxltrxn.githubclient.presentation.repositoriesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.zxltrxn.githubclient.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(R.layout.fragment_repositories_list) {
    private var _binding: FragmentRepositoriesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RepositoriesListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentRepositoriesListBinding.inflate(inflater, container, false)
        val view = binding.apply {
            rvRepositoriesList.addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }
        (requireActivity() as MainActivity).supportActionBar?.run {
            title = getString(R.string.repositories_list_header)
            setDisplayHomeAsUpEnabled(false)
            show()
        }
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = setUpAdapter()
        observe(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvRepositoriesList.adapter = null
        _binding = null
    }

    private fun setUpAdapter(): RepositoriesAdapter {
        val adapter = RepositoriesAdapter { id, name ->
            navigateToDetailInfo(id, name)
        }
        binding.rvRepositoriesList.adapter = adapter
        return adapter
    }

    private fun observe(adapter: RepositoriesAdapter) {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            if (state is State.Loaded) adapter.submitList(state.repos)
            binding.rvRepositoriesList.visibility =
                if (state is State.Loaded) View.VISIBLE else View.GONE

            binding.tvRepositoriesError.visibility =
                if (state is State.Error || state is State.Empty) View.VISIBLE else View.GONE
            binding.tvRepositoriesError.text = if (state is State.Error) state.error else null
            binding.tvRepositoriesError.text =
                if (state is State.Empty) getString(R.string.empty_repositories_list) else null
        }
    }

    private fun navigateToDetailInfo(repoId: Int, repoName: String) {
        val action = RepositoriesListFragmentDirections
            .repositoriesListFragmentToDetailInfoFragment(repoId = repoId, repoName = repoName)
        this.findNavController().navigate(action)
    }
}