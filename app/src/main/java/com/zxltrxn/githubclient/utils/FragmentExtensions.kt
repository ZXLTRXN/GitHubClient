package com.zxltrxn.githubclient.utils

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.zxltrxn.githubclient.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> Fragment.collectLifecycleFlow(flow: Flow<T>, collect: FlowCollector<T>) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}

fun Fragment.signOut(viewModelSignOut:()-> Unit) {
    viewModelSignOut()
    val navOption = NavOptions
        .Builder()
        .setPopUpTo(R.id.repositoriesListFragment, true)
        .setExitAnim(R.anim.slide_out_right)
        .setEnterAnim(R.anim.slide_in_left)
        .build()
    findNavController().navigate(
        resId = R.id.authFragment,
        args = Bundle.EMPTY, navOptions = navOption
    )
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT){
    context?.let{
        Toast.makeText(context, message, duration).show()
    }
}