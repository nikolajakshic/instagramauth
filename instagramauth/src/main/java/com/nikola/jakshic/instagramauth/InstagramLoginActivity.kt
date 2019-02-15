@file:Suppress("DEPRECATION")

package com.nikola.jakshic.instagramauth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.TypedArray
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@SuppressLint("SetJavaScriptEnabled")
internal class InstagramLoginActivity : AppCompatActivity() {

    private val AUTH_URL = "https://api.instagram.com/oauth/authorize/?response_type=token"
    private val PARAM_CLIENT_ID = "client_id"
    private val PARAM_REDIRECT_URI = "redirect_uri"

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var webView: WebView

    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        swipeRefresh = SwipeRefreshLayout(this)
        webView = WebView(this)

        swipeRefresh.addView(webView)

        // If network error happens, default error page is shown, which leaks CLIENT ID,
        // so we need to make WebView invisible to prevent that. Whether error happened or not
        // is determined in [InstagramWebViewClient], which is also where visibility toggling is happening.
        webView.visibility = View.INVISIBLE

        setContentView(swipeRefresh)

        getStyleAttrs()

        setTitle(title)

        webView.webViewClient = InstagramWebViewClient(loginCallback, pageCallback)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        val uri = buildUri()

        webView.loadUrl(uri)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val pageCallback = object : InstagramWebViewClient.PageCallback {
        override fun onLoadingStarted() {
            swipeRefresh.isEnabled = true
            swipeRefresh.isRefreshing = true
        }

        override fun onLoadingFinished() {
            swipeRefresh.isRefreshing = false
            swipeRefresh.isEnabled = false
        }
    }

    private val loginCallback = object : InstagramWebViewClient.LoginCallback {
        override fun onSuccess(token: String) {
            val data = Intent()
            data.putExtra(AuthManager.BUNDLE_TOKEN, token)
            setResult(AuthManager.RESULT_SUCCESS, data)
            finish()
        }

        override fun onError(e: InstagramAuthException) {
            when (e) {
                is InstagramAuthNetworkOperationException -> setResult(AuthManager.RESULT_NETWORK_ERROR)
                is InstagramAuthAccessDeniedException -> setResult(AuthManager.RESULT_ACCESS_DENIED)
            }
            finish()
        }
    }

    private fun buildUri(): String {
        val clientId = AuthManager.getInstance().getClientId()
        val redirectUrl = AuthManager.getInstance().getRedirectUri()

        val uri = Uri.parse(AUTH_URL).buildUpon()
                .appendQueryParameter(PARAM_CLIENT_ID, clientId)
                .appendQueryParameter(PARAM_REDIRECT_URI, redirectUrl)
                .build()

        return uri.toString()
    }

    private fun getStyleAttrs() {
        val typedArray: TypedArray = obtainStyledAttributes(R.style.InstagramAuthTheme, R.styleable.InstagramAuth)

        try {
            title = typedArray.getString(R.styleable.InstagramAuth_instagram_auth_title)
        } finally {
            typedArray.recycle()
        }
    }
}