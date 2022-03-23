package com.zxltrxn.githubclient.presentation.repositoriesList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.databinding.RepositoryElementBinding

class RepositoriesAdapter(
    private val repos: List<Repo>
    ) : RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = RepositoryElementBinding
            .inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val el = repos[position]
        viewHolder.bind(el)
    }

    override fun getItemCount() = repos.size

    class ViewHolder(
        private val binding: RepositoryElementBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(el: Repo){
            bindName(el.name)
            bindLanguage(el.language)
            bindDescription(el.description)
        }

        private fun bindName(text:String?){
            binding.rvElementRepoName.text = text
        }

        private fun bindLanguage(text:String?){
            binding.rvElementRepoName.text = text
        }

        private fun bindDescription(text:String?){
            binding.rvElementRepoName.text = text
        }

    }
}