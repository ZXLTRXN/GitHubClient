package com.zxltrxn.githubclient.presentation.repositoriesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(R.layout.fragment_repositories_list) {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentAuthBinding.inflate(inflater,container,false)
//        val view = binding.apply {
//            lifecycleOwner = viewLifecycleOwner
//            vm = viewModel
//        }.root

//        val navController = findNavController()
//        userViewModel.user.observe(viewLifecycleOwner, Observer { user ->
//            if (user != null) {
//                showWelcomeMessage()
//            } else {
//                navController.navigate(R.id.login_fragment)
//            }
//        })
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}