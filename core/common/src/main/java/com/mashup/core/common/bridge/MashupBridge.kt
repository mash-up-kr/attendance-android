package com.mashup.core.common.bridge

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class MashupBridge(
    private val context: Context,
    private val onBackPressed: () -> Unit = {},
    private val onNavigateDanggn: () -> Unit = {}
) : MashupBridgeInterface() {
    @JavascriptInterface
    override fun showToast(toast: String) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    override fun step(type: String) {
        when (Type.values().find { it.name == type.uppercase() }) {
            Type.BACK -> onBackPressed()
            Type.DANGGN -> onNavigateDanggn()
            else -> {}
        }
    }

    companion object {
        const val name = "MashupBridge"
    }
}

abstract class MashupBridgeInterface {
    open fun showToast(toast: String) {}
    open fun step(type: String) {}
}

enum class Type {
    BACK, DANGGN
}
