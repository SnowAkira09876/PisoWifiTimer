package com.akira.pisowifitimer.feature_home.util

import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import javax.inject.Inject

class AppChromeClient @Inject constructor(): WebChromeClient() {
    override fun onJsAlert(
        view: WebView,
        url: String,
        message: String,
        result: JsResult
    ): Boolean {
        return true
    }
}