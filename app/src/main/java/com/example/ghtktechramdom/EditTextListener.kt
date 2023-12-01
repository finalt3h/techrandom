package com.example.ghtktechramdom

import android.text.Editable
import android.text.TextWatcher

class EditTextListener : TextWatcher {
    private var callback: ((String) -> Unit)? = null
    fun updateData(callback: ((String) -> Unit)? = null) {
        this.callback = callback
    }
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable?) {
        callback?.invoke(editable.toString())
    }
}
