package com.zxltrxn.githubclient.utils

import com.zxltrxn.githubclient.data.model.RepoData
import com.zxltrxn.githubclient.domain.model.Repo

fun RepoData.toRepo(): Repo {
    return Repo(
        id = this.id,
        owner = this.owner.toRepoOwner(),
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

fun RepoData.Owner.toRepoOwner(): Repo.Owner {
    return Repo.Owner(name = this.name)
}