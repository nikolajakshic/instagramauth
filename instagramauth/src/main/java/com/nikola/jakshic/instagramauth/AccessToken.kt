package com.nikola.jakshic.instagramauth

internal interface AccessToken {

    fun saveToken(token: String)

    fun getToken(): String?

    fun hasToken(): Boolean

    fun removeToken()
}