package com.zxltrxn.githubclient.presentation.detailInfo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.utils.Constants.TAG

class DetailInfoFragment : Fragment(R.layout.fragment_detail_info) {

    private val args: DetailInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ${args.repoId}")
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}