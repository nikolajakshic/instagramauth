package com.nikola.jakshic.instagramauth

internal interface InstagramService {

    fun getUserInfo(token: String, callback: InstagramService.Callback<UserInfo>)

    fun getUserInfoAsync(token: String, callback: InstagramService.Callback<UserInfo>)

    interface Callback<T> {
        fun onSuccess(item: T)
        fun onError(e: InstagramAuthException)
    }
}