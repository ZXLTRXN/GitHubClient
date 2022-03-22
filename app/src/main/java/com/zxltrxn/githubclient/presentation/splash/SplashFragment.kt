package com.zxltrxn.githubclient.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zxltrxn.githubclient.R
import com.zxltrxn.githubclient.presentation.MainActivity

class SplashFragment : Fragment(R.layout.fragment_splash) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).supportActionBar?.hide()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}