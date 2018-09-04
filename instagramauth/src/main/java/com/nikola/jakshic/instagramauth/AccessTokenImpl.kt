package com.nikola.jakshic.instagramauth

import android.content.Context
import android.text.TextUtils

internal class AccessTokenImpl(context: Context) : AccessToken {

    private val PREFS_NAME = "instagram_auth_prefs"
    private val PREF_ACCESS_TOKEN = "access_token"

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveToken(token: String) {
        prefs.edit().putString(PREF_ACCESS_TOKEN, token).apply()
    }

    override fun getToken(): String? {
        return prefs.getString(PREF_ACCESS_TOKEN, null)
    }

    override fun hasToken(): Boolean {
        val token = prefs.getString(PREF_ACCESS_TOKEN, null)
        return !TextUtils.isEmpty(token)
    }

    override fun removeToken() {
        prefs.edit().clear().apply()
    }
}