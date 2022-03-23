package com.zxltrxn.githubclient.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Repo(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val language: String? = null,
//    val owner:String? = null
)
