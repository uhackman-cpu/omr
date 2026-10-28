package com.dev.omr.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

object ClipUtils {
    fun copy(context: Context, text: String) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText("OMR", text))
    }

    fun share(context: Context, text: String) {
        val i = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(i, null))
    }
}
