package com.zxltrxn.githubclient.presentation.detailInfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.databinding.FragmentDetailInfoBinding
import com.zxltrxn.githubclient.presentation.MainActivity
import com.zxltrxn.githubclient.presentation.detailInfo.RepositoryInfoViewModel.State
import com.zxltrxn.githubclient.presentation.detailInfo.RepositoryInfoViewModel.ReadmeState
import com.zxltrxn.githubclient.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

@AndroidEntryPoint
class DetailInfoFragment : Fragment(R.layout.fragment_detail_info) {
    private var _binding: FragmentDetailInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RepositoryInfoViewModel>()

    private val args: DetailInfoFragmentArgs by navArgs()

    private var markwon: Markwon? = null

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
        markwon = Markwon.create(requireContext())
        viewModel.getInfo(args.repoId)

        (requireActivity() as MainActivity).supportActionBar?.let{
            it.title = args.repoName
            it.show()
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
        markwon = null
    }

    private fun observe(){
        collectLatestLifecycleFlow(viewModel.state){ state ->
            when (state){
                is State.Error -> binding.tvInfoError.text = state.error
                is State.Loaded ->{
                    with(binding){
                        tvLicense.text = state.githubRepo.license?.name ?: "-"
                        tvStars.text = state.githubRepo.stars.toString()
                        tvForks.text = state.githubRepo.forks.toString()
                        tvWatchers.text = state.githubRepo.watchers.toString()
                        tvLink.text = state.githubRepo.htmlUrl
                        tvLink.setOnClickListener{
                            state.githubRepo.htmlUrl?.let { url -> openUrl(url) }
                        }
                    }
                    when (val readmeState = state.readmeState){
                        is ReadmeState.Error -> binding.tvReadme.text = readmeState.error
                        is ReadmeState.Empty -> binding.tvReadme.text = getString(R.string.empty_readme)
                        is ReadmeState.Loaded -> {
                            markwon?.setMarkdown(binding.tvReadme, readmeState.markdown)
                        }
                        is ReadmeState.Loading -> {}
                    }
                }
                else -> {}
            }
        }
    }

    private fun openUrl(url: String){
        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
        startActivity(intent)
    }
}