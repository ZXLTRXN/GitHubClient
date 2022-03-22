package com.zxltrxn.githubclient.presentation.repositoriesList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.databinding.FragmentAuthBinding
import com.zxltrxn.githubclient.databinding.FragmentRepositoriesListBinding
import com.zxltrxn.githubclient.presentation.MainActivity
import com.zxltrxn.githubclient.presentation.auth.AuthFragmentDirections
import com.zxltrxn.githubclient.utils.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(R.layout.fragment_repositories_list) {
    private var _binding: FragmentRepositoriesListBinding? = null
    private val binding get() = _binding!!

//    private val args: RepositoriesListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val actionBar = (requireActivity() as MainActivity).supportActionBar
        actionBar?.show()
        actionBar?.title = getString(R.string.repositories_list_header)

        _binding = FragmentRepositoriesListBinding.inflate(inflater,container,false)

        binding.btnTest.setOnClickListener{
            navigateToDetailInfo()
        }
//        Log.d(TAG, "onCreateView repos: ${args.user}")

//        val view = binding.apply {
//            lifecycleOwner = viewLifecycleOwner
//            vm = viewModel
//        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToDetailInfo(){
        val action = RepositoriesListFragmentDirections
            .repositoriesListFragmentToDetailInfoFragment()
        this.findNavController().navigate(action)
    }
}