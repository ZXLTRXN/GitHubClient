package com.zxltrxn.githubclient.utils

import com.zxltrxn.githubclient.presentation.auth.AuthViewModel.ValidationState
import java.util.regex.Pattern

fun String.validateToken(): ValidationState {
    val pattern = "^[a-zA-Z0-9_-]{0,45}$"
    return if (this.isEmpty()) ValidationState.EMPTY
    else {
        val matcher = Pattern.compile(pattern).matcher(this)
        if (matcher.matches()) ValidationState.VALID
        else ValidationState.INVALID
    }

}