package com.zxltrxn.githubclient.domain.model

data class Repo(
    val id: Int,
    val owner: Owner,
    val name: String,
    val htmlUrl: String,
    val description: String? = null,
    val language: String? = null,
    val license: License? = null,
    var languageColor: String? = null,
    val forks: Int = 0,
    val stars: Int = 0,
    val watchers: Int = 0,
    val branch: String = "master",
) {
    data class License(val name: String)
    data class Owner(val name: String)
}

