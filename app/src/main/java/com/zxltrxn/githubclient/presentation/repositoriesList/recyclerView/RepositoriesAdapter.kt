package com.zxltrxn.githubclient.presentation.repositoriesList.recyclerView

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.zxltrxn.githubclient.data.model.Repo
import com.zxltrxn.githubclient.databinding.RepositoryElementBinding
import com.zxltrxn.githubclient.utils.Constants.TAG


class RepositoriesAdapter(
    private val onItemClick:(Int)->Unit
) : ListAdapter<Repo, RepositoriesAdapter.RepoViewHolder>(RepoDiffUtilCallback()) {

    class RepoViewHolder(
        private val binding: RepositoryElementBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo) {
            bindName(repo.name)
            bindLanguage(repo.language, repo.languageColor)
            bindDescription(repo.description)
        }

        private fun bindName(text: String?) {
            binding.rvElementRepoName.text = text
        }

        private fun bindLanguage(text: String?, color: String?) {
            binding.rvElementRepoLanguage.let{
                it.text = text
                color?.let{ color ->
                    it.setTextColor(Color.parseColor(color))
                }
            }
        }

        private fun bindDescription(text: String?) {
            if(text == null){
                binding.rvElementRepoDescription.visibility = View.GONE
            }else{
                binding.rvElementRepoDescription.text = text
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = RepositoryElementBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        getItem(position).let{ repo ->
            holder.bind(repo)
            holder.itemView.setOnClickListener{
                onItemClick(repo.id!!)
            }
        }
    }
}







//class RepositoriesAdapter(
//    private val repos: List<Repo>
//    ) : RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
//        val view = RepositoryElementBinding
//            .inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
//        val el = repos[position]
//        viewHolder.bind(el)
//    }
//
//    override fun getItemCount() = repos.size
//
//    class ViewHolder(
//        private val binding: RepositoryElementBinding,
//    ) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(el: Repo){
//            bindName(el.name)
//            bindLanguage(el.language)
//            bindDescription(el.description)
//        }
//
//        private fun bindName(text:String?){
//            binding.rvElementRepoName.text = text
//        }
//
//        private fun bindLanguage(text:String?){
//            binding.rvElementRepoName.text = text
//        }
//
//        private fun bindDescription(text:String?){
//            binding.rvElementRepoName.text = text
//        }
//
//    }
//}