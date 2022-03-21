package com.zxltrxn.githubclient.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String? = null,
    val login: String? = null
)