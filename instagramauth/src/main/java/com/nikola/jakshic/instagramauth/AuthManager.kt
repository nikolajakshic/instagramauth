package com.nikola.jakshic.instagramauth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import android.webkit.CookieManager

class AuthManager internal constructor(private val service: InstagramService, private val accessToken: AccessToken, private val appInfo: AppInfo) {

    private var loginCallback: LoginCallback? = null

    companion object {
        // Singleton Instance
        private var instance: AuthManager? = null

        @JvmSynthetic internal const val LOGIN_REQUEST = 140
        @JvmSynthetic internal const val RESULT_ACCESS_DENIED = 300
        @JvmSynthetic internal const val RESULT_NETWORK_ERROR = 310
        @JvmSynthetic internal const val RESULT_SUCCESS = 320
        @JvmSynthetic internal const val BUNDLE_TOKEN = "access_token"

        @JvmStatic
        fun getInstance(): AuthManager {
            if (instance == null) throw InstagramAuthNotInitializedException()
            return instance!!
        }

        @JvmSynthetic
        internal fun initialize(context: Context) {
            if (AuthManager.instance != null) return

            // Use Application context to prevent memory leaks
            val appContext = context.applicationContext

            val client = HttpClient(InstagramServiceImpl.BASE_URL)
            val converter = JsonConverter
            val service = InstagramServiceImpl(client, converter)
            val accessToken = AccessTokenImpl(appContext)
            val appInfo = AppInfo(appContext)

            AuthManager.instance = AuthManager(service, accessToken, appInfo)
        }
    }

    interface LoginCallback {
        fun onSuccess()
        fun onError(e: InstagramAuthException)
    }

    interface Callback<T : Any> {
        fun onSuccess(item: T)
        fun onError(e: InstagramAuthException)
    }

    fun login(fragment: Fragment, callback: LoginCallback) {
        if (isLoggedIn()) return
        this.loginCallback = callback
        fragment.startActivityForResult(Intent(fragment.activity, InstagramLoginActivity::class.java), LOGIN_REQUEST)
    }

    fun login(fragment: android.app.Fragment, callback: LoginCallback) {
        if (isLoggedIn()) return
        this.loginCallback = callback
        fragment.startActivityForResult(Intent(fragment.activity, InstagramLoginActivity::class.java), LOGIN_REQUEST)
    }

    fun login(activity: Activity, callback: LoginCallback) {
        if (isLoggedIn()) return
        this.loginCallback = callback
        activity.startActivityForResult(Intent(activity, InstagramLoginActivity::class.java), LOGIN_REQUEST)
    }

    fun isLoggedIn() = accessToken.hasToken()

    fun logout() {
        CookieManager.getInstance().removeAllCookie()
        accessToken.removeToken()
    }

    fun getUserInfo(callback: Callback<UserInfo>) {
        if (!isLoggedIn()) {
            callback.onError(InstagramAuthNotLoggedInException())
            return
        }

        val token = accessToken.getToken()!!

        service.getUserInfo(token, object : InstagramService.Callback<UserInfo> {
            override fun onSuccess(item: UserInfo) {
                callback.onSuccess(item)
            }

            override fun onError(e: InstagramAuthException) {
                // If token has been invalidated, remove it from the cache
                if (e is InstagramAuthTokenException) logout()
                callback.onError(e)
            }
        })
    }

    fun getUserInfoAsync(callback: Callback<UserInfo>) {
        if (!isLoggedIn()) {
            callback.onError(InstagramAuthNotLoggedInException())
            return
        }

        val token = accessToken.getToken()!!

        service.getUserInfoAsync(token, object : InstagramService.Callback<UserInfo> {
            override fun onSuccess(item: UserInfo) {
                callback.onSuccess(item)
            }

            override fun onError(e: InstagramAuthException) {
                // If token has been invalidated, remove it from the cache
                if (e is InstagramAuthTokenException) logout()
                callback.onError(e)
            }
        })
    }

    fun getToken() = accessToken.getToken()

    @JvmSynthetic internal fun getRedirectUri() = appInfo.getRedirectUri()

    @JvmSynthetic internal fun getClientId() = appInfo.getClientId()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AuthManager.LOGIN_REQUEST && resultCode == RESULT_SUCCESS) {
            val token = data?.getStringExtra(AuthManager.BUNDLE_TOKEN)
            accessToken.saveToken(token!!)
            loginCallback?.onSuccess()
            return
        }

        if (requestCode == AuthManager.LOGIN_REQUEST && resultCode == RESULT_ACCESS_DENIED) {
            loginCallback?.onError(InstagramAuthAccessDeniedException())
            return
        }

        if (requestCode == AuthManager.LOGIN_REQUEST && resultCode == RESULT_NETWORK_ERROR) {
            loginCallback?.onError(InstagramAuthNetworkOperationException())
            return
        }
    }
}
