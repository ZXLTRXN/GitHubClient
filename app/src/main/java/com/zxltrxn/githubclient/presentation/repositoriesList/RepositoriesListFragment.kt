package com.zxltrxn.githubclient.presentation.repositoriesList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.databinding.FragmentAuthBinding
import com.zxltrxn.githubclient.databinding.FragmentRepositoriesListBinding
import com.zxltrxn.githubclient.presentation.MainActivity
import com.zxltrxn.githubclient.presentation.auth.AuthFragmentDirections
import com.zxltrxn.githubclient.presentation.auth.AuthViewModel
import com.zxltrxn.githubclient.utils.Constants.TAG
import com.zxltrxn.githubclient.utils.collectLatestLifecycleFlow
import com.zxltrxn.githubclient.utils.collectLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(R.layout.fragment_repositories_list) {
    private var _binding: FragmentRepositoriesListBinding? = null
    private val binding get() = _binding!!
//    private val args: RepositoriesListFragmentArgs by navArgs()
    private val viewModel by viewModels<RepositoriesListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentRepositoriesListBinding.inflate(inflater,container,false)

//        adapter = RepositoriesAdapter(viewModel.state)
//        binding.rvRepositoriesList.run {
//            adapter =
//        }

        (requireActivity() as MainActivity).supportActionBar?.run{
            show()
            title = getString(R.string.repositories_list_header)
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun observe(){
//        collectLatestLifecycleFlow(viewModel.state){
//        }
//    }

    private fun navigateToDetailInfo(){
        val action = RepositoriesListFragmentDirections
            .repositoriesListFragmentToDetailInfoFragment()
        this.findNavController().navigate(action)
    }
}