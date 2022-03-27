package com.zxltrxn.githubclient.presentation.detailInfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.databinding.FragmentDetailInfoBinding
import com.zxltrxn.githubclient.presentation.MainActivity
import com.zxltrxn.githubclient.utils.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailInfoFragment : Fragment(R.layout.fragment_detail_info) {
    private var _binding: FragmentDetailInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RepositoryInfoViewModel>()

    private val args: DetailInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDetailInfoBinding.inflate(inflater,container,false)
        val view = binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }

        Log.d(TAG, "onCreateView: ${args.repoId}")

        (requireActivity() as MainActivity).supportActionBar?.show()
        return view.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}