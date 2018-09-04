package com.nikola.jakshic.instagramauth

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils

internal class AppInfo(private val context: Context) {

    private val CLIENT_ID = "com.nikola.jakshic.instagramauth.ClientId"
    private val REDIRECT_URI = "com.nikola.jakshic.instagramauth.RedirectUri"

    fun getClientId(): String {
        val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val meta: Bundle? = appInfo.metaData

        val clientId = meta?.getString(CLIENT_ID)

        if (TextUtils.isEmpty(clientId)) throw InstagramAuthInvalidClientIdException()

        return clientId!!
    }

    fun getRedirectUri(): String {
        val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val meta: Bundle? = appInfo.metaData

        val redirectUri = meta?.getString(REDIRECT_URI)

        if (TextUtils.isEmpty(redirectUri)) throw InstagramAuthInvalidRedirectUriException()

        return redirectUri!!
    }
}