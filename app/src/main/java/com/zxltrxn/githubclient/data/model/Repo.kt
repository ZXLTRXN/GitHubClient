package com.zxltrxn.githubclient.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Repo(
    val id: Int? = null,
    val name: String? = null,
    @SerialName("html_url") val htmlUrl:String? = null,
    val description: String? = null,
    val language: String? = null,
    @Transient var languageColor: String? = null,

    @SerialName("forks_count") val forks: Int = 0,
    @SerialName("stargazers_count") val stars: Int = 0,
    @SerialName("watchers_count") val watchers: Int = 0,
    val license:License? = null,

    @SerialName("default_branch") val branch: String? = null,
    val owner: Owner? = null
){
    @Serializable
    data class License(val name: String? = null)

    @Serializable
    data class Owner(
        @SerialName("login") val name: String? = null)
}

