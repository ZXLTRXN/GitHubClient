package com.zxltrxn.githubclient.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun EditText.bindTextTwoWay(stateFlow: MutableStateFlow<String>, lifecycleOwner: LifecycleOwner) {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            stateFlow.value = s.toString()
        }
    }

    this.addTextChangedListener(textWatcher)

    lifecycleOwner.lifecycleScope.launch {
        stateFlow.collectLatest { text ->
            if (this@bindTextTwoWay.text.toString() != text)
                this@bindTextTwoWay.setText(text)
        }
    }
}