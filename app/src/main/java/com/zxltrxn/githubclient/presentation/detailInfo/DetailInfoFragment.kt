package com.zxltrxn.githubclient.presentation.detailInfo

import android.content.ActivityNotFoundException
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
import com.zxltrxn.githubclient.presentation.detailInfo.RepositoryInfoViewModel.ReadmeState
import com.zxltrxn.githubclient.presentation.detailInfo.RepositoryInfoViewModel.State
import com.zxltrxn.githubclient.utils.collectLatestLifecycleFlow
import com.zxltrxn.githubclient.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

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
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDetailInfoBinding.inflate(inflater, container, false)

        viewModel.getInfo(args.repoId)

        (requireActivity() as MainActivity).supportActionBar?.run {
            title = args.repoName
            setDisplayHomeAsUpEnabled(true)
            show()
        }
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

    private fun observe() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            binding.tvInfoError.visibility = if (state is State.Error) View.VISIBLE else View.GONE
            binding.tvInfoError.text = if (state is State.Error) state.error else null

            binding.progressCircular.visibility =
                if (state is State.Loading) View.VISIBLE else View.GONE

            binding.allSections.visibility = if (state is State.Loaded) View.VISIBLE else View.GONE
            binding.tvLicense.text =
                if (state is State.Loaded) state.githubRepo.license?.name ?: "-" else null
            binding.tvStars.text =
                if (state is State.Loaded) state.githubRepo.stars.toString() else null
            binding.tvForks.text =
                if (state is State.Loaded) state.githubRepo.forks.toString() else null
            binding.tvWatchers.text =
                if (state is State.Loaded) state.githubRepo.watchers.toString() else null
            binding.tvLink.text = if (state is State.Loaded) state.githubRepo.htmlUrl else null
            if (state is State.Loaded) {
                binding.tvLink.setOnClickListener {
                    openUrl(state.githubRepo.htmlUrl)
                }
            }
            if (state is State.Loaded) {
                val readmeState = state.readmeState
                binding.tvReadme.text =
                    if (readmeState is ReadmeState.Error) readmeState.error else null
                binding.tvReadme.text =
                    if (readmeState is ReadmeState.Empty) getString(R.string.empty_readme) else null

                if (readmeState is ReadmeState.Loaded) {
                    context?.let { context ->
                        val markwon = Markwon.create(context)
                        markwon.setMarkdown(binding.tvReadme, readmeState.markdown)
                    }
                }
            }
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
            startActivity(intent)
        } catch (e: NullPointerException) {
            showToast(getString(R.string.bad_url_repo))
        } catch (e: ActivityNotFoundException) {
            showToast(getString(R.string.bad_url_repo))
        }
    }
}