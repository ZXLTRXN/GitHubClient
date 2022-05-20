package com.zxltrxn.githubclient.presentation.detailInfo

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.databinding.FragmentDetailInfoBinding
import com.zxltrxn.githubclient.presentation.detailInfo.RepositoryInfoViewModel.ReadmeState
import com.zxltrxn.githubclient.presentation.detailInfo.RepositoryInfoViewModel.State
import com.zxltrxn.githubclient.utils.collectLatestLifecycleFlow
import com.zxltrxn.githubclient.utils.showToast
import com.zxltrxn.githubclient.utils.signOut
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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar(args.repoName)
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpToolbar(title: String) {
        binding.run {
            toolbar.root.title = title
            toolbar.root.setTitleTextAppearance(
                requireContext(),
                R.style.TextAppearance_Widget_Toolbar_Title
            )
            toolbar.root.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbar.root.setNavigationOnClickListener {
                navigateBack()
            }
            toolbar.root.setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.action_sign_out) {
                    signOut { viewModel.signOut() }
                }
                super.onOptionsItemSelected(menuItem)
            }
        }
    }


    private fun setUpViews(state: State) {
        with(binding) {
            errorLayout.root.visibility = if (state is State.Error) View.VISIBLE else View.GONE
            errorLayout.tvLabelError.text =
                if (state is State.Error) state.errorLabel.getString(requireContext()) else null
            errorLayout.tvInfoError.text =
                if (state is State.Error) state.errorMessage.getString(requireContext()) else null
            if (state is State.Error) {
                errorLayout.ivError.setImageResource(state.errorIcon)
            }

            loadingLayout.root.visibility =
                if (state is State.Loading) View.VISIBLE else View.GONE

            content.visibility = if (state is State.Loaded) View.VISIBLE else View.GONE
            allSections.visibility = if (state is State.Loaded) View.VISIBLE else View.GONE
            tvLicense.text =
                if (state is State.Loaded) state.githubRepo.license?.name ?: "-" else null
            tvStars.text =
                if (state is State.Loaded) state.githubRepo.stars.toString() else null
            tvForks.text =
                if (state is State.Loaded) state.githubRepo.forks.toString() else null
            tvWatchers.text =
                if (state is State.Loaded) state.githubRepo.watchers.toString() else null
            tvLink.text = if (state is State.Loaded) state.githubRepo.htmlUrl else null
            if (state is State.Loaded) {
                tvLink.setOnClickListener {
                    openUrl(state.githubRepo.htmlUrl)
                }
            }
            if (state is State.Loaded) {
                val readmeState = state.readmeState

                progressCircularReadme.visibility =
                    if (readmeState is ReadmeState.Loading) View.VISIBLE else View.GONE

                if (readmeState is ReadmeState.Error) exposeErrorConstraintsForReadme()

                errorLayout.root.visibility =
                    if (readmeState is ReadmeState.Error) View.VISIBLE else View.GONE
                errorLayout.tvLabelError.text =
                    if (readmeState is ReadmeState.Error) readmeState.errorLabel.getString(
                        requireContext()
                    ) else null
                errorLayout.tvInfoError.text =
                    if (readmeState is ReadmeState.Error) readmeState.errorMessage.getString(
                        requireContext()
                    ) else null
                if (readmeState is ReadmeState.Error) {
                    errorLayout.ivError.setImageResource(readmeState.errorIcon)
                }

                if (readmeState is ReadmeState.Empty) tvReadme.text =
                    getString(R.string.empty_readme)

                if (readmeState is ReadmeState.Loaded) {
                    context?.let { context ->
                        val markwon = Markwon.create(context)
                        markwon.setMarkdown(tvReadme, readmeState.markdown)
                    }
                }
            }
        }
    }

    private fun exposeErrorConstraintsForReadme() {
        with(binding) {
            val constraintLayout: ConstraintLayout = parentLayout
            ConstraintSet().apply {
                clone(constraintLayout)
                connect(
                    errorLayout.root.id, ConstraintSet.TOP,
                    toolbar.root.id, ConstraintSet.BOTTOM, 0
                )
                applyTo(constraintLayout)
            }
        }
    }

    private fun observe() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            setUpViews(state)
        }
        binding.errorLayout.retryButton.setOnClickListener { viewModel.retry() }
    }

    private fun navigateBack() {
        findNavController().navigateUp()
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