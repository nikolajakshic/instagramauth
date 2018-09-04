package com.nikola.jakshic.instagramauth

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient

@Suppress("OverridingDeprecatedMember")
internal class InstagramWebViewClient(private val loginCallback: LoginCallback, private val pageCallback: PageCallback) : WebViewClient() {

    private val PARAM_TOKEN = "access_token"
    private var errorHappened = false

    interface PageCallback {
        fun onLoadingStarted()
        fun onLoadingFinished()
    }

    interface LoginCallback {
        fun onSuccess(token: String)
        fun onError(e: InstagramAuthException)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url != null && AuthManager.getInstance().getRedirectUri().contains(Uri.parse(url).authority)) {

            val fragment: String? = Uri.parse(url).fragment

            if (fragment == null) {
                loginCallback.onError(InstagramAuthAccessDeniedException())
            } else {
                val token = fragment.removePrefix("$PARAM_TOKEN=")
                loginCallback.onSuccess(token)
            }
            return true
        }

        return false
    }

    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        errorHappened = true
        loginCallback.onError(InstagramAuthNetworkOperationException())
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        view?.visibility = View.INVISIBLE
        pageCallback.onLoadingStarted()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        pageCallback.onLoadingFinished()
        if (!errorHappened) view?.visibility = View.VISIBLE
    }
}