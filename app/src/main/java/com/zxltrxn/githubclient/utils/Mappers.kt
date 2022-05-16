package com.zxltrxn.githubclient.utils


import com.zxltrxn.githubclient.data.model.RepoData
import com.zxltrxn.githubclient.domain.model.Repo
import com.zxltrxn.githubclient.domain.model.UserInfo

fun RepoData.Owner.toUserInfo(): UserInfo = UserInfo(name = this.name)

fun RepoData.toRepo(): Repo {
    return Repo(
        id = this.id,
        name = this.name,
        htmlUrl = this.htmlUrl,
        description = this.description,
        language = this.language,
        license = this.license?.toRepoLicense(),
        languageColor = "#FF0000",
        forks = this.forks,
        stars = this.stars,
        watchers = this.watchers,
        branch = this.branch
    )
}

fun RepoData.License.toRepoLicense(): Repo.License {
    return Repo.License(name = this.name)
}