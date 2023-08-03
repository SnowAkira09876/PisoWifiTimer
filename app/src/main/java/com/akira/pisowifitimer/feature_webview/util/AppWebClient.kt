package com.akira.pisowifitimer.feature_webview.util

import android.annotation.SuppressLint
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.webkit.WebResourceErrorCompat
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewFeature

class AppWebClient: WebViewClientCompat() {
    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(
        view: WebView,
        handler: SslErrorHandler,
        error: SslError
    ) {
        handler.proceed()
    }

    @Deprecated("Deprecated in Java")
    override fun onReceivedError(
        webview: WebView, errorCode: Int, description: String, failingUrl: String
    ) {
        if (errorCode == ERROR_CONNECT && webview.url?.startsWith("https://") == true) {
            val httpUrl = "http://" + webview.url?.substring(8)
            webview.loadUrl(httpUrl)
        }
    }

    override fun onReceivedError(
        webView: WebView,
        request: WebResourceRequest,
        error: WebResourceErrorCompat
    ) {
        if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_RESOURCE_ERROR_GET_CODE)
            && error.errorCode == ERROR_CONNECT
            && webView.url?.startsWith("https://") == true
        ) {
            val httpUrl = "http://" + webView.url?.substring(8)
            webView.loadUrl(httpUrl)
        }
    }
}