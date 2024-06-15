package com.mashup.ui.webview.tester

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class MashupBridge(private val context: Context) {
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }
}