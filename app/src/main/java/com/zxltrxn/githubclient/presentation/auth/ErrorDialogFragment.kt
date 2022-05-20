package com.zxltrxn.githubclient.presentation.auth

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.data.network.APIService
import com.zxltrxn.githubclient.data.network.NetworkUtils
import com.zxltrxn.githubclient.domain.LocalizeString


class ErrorDialogFragment(private val message: LocalizeString, private val code: Int?) :
    DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertMessage =
            if (code == null || code == NetworkUtils.NO_INTERNET_CODE || code == APIService.WRONG_TOKEN_CODE)
                message.getString(requireContext())
            else
                "${message.getString(requireContext())} / $code"

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.something_error_label))
            .setMessage(alertMessage)
            .setPositiveButton("ok") { _, _ -> }
            .create()
    }

    companion object {
        const val TAG = "ErrorDialogFragment"
    }
}