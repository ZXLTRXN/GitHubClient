package com.zxltrxn.githubclient.presentation.auth

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.domain.LocalizeString


class ErrorDialogFragment(private val message: LocalizeString, private val code: Int?) :
    DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = "${message.getString(requireContext())} / $code"
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.something_error_label))
            .setMessage(message)
            .setPositiveButton("ok") { _, _ -> }
            .create()
    }

    companion object {
        const val TAG = "ErrorDialogFragment"
    }
}