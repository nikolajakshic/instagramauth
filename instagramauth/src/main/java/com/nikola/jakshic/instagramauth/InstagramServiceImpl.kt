package com.nikola.jakshic.instagramauth

import android.os.NetworkOnMainThreadException

internal class InstagramServiceImpl(private val httpClient: HttpClient, private val converter: JsonConverter) : InstagramService {

    companion object {
        internal const val BASE_URL = "https://api.instagram.com/v1/"
    }

    private val executors = AppExecutors()

    private val PARAM_TOKEN = "access_token"
    private val PATH_USERS = "users"
    private val PATH_SELF = "self"

    override fun getUserInfo(token: String, callback: InstagramService.Callback<UserInfo>) {
        val paths = HttpPath()
        paths.addPath(PATH_USERS)
        paths.addPath(PATH_SELF)

        val params = HttpParam()
        params.addParam(PARAM_TOKEN, token)

        val response = httpClient.request(paths, params)

        if (response.error is NetworkOnMainThreadException)
            throw NetworkOnMainThreadException()

        if (response.error != null) {
            callback.onError(InstagramAuthNetworkOperationException())
            return
        }

        if (response.isSuccess()) {
            val userInfo = converter.toUserInfo(response.responseBody!!)
            callback.onSuccess(userInfo)
            return
        }
        try {
            // If an error occurs, server might or might not send the meta, if there is a meta,
            // that's the Invalid Token Error, if there is not, that's some network operation error.
            val error = converter.toError(response.responseBody!!)
            callback.onError(InstagramAuthTokenException(error.message))
        } catch (e: Exception) {
            callback.onError(InstagramAuthNetworkOperationException())
        }
    }

    override fun getUserInfoAsync(token: String, callback: InstagramService.Callback<UserInfo>) {
        val paths = HttpPath()
        paths.addPath(PATH_USERS)
        paths.addPath(PATH_SELF)

        val params = HttpParam()
        params.addParam(PARAM_TOKEN, token)

        executors.networkIO.execute {
            val response = httpClient.request(paths, params)
            executors.mainThread.execute {
                if (response.error != null) {
                    callback.onError(InstagramAuthNetworkOperationException())
                    return@execute
                }

                if (response.isSuccess()) {
                    val userInfo = converter.toUserInfo(response.responseBody!!)
                    callback.onSuccess(userInfo)
                    return@execute
                }
                try {
                    // If an error occurs, server might or might not send the meta, if there is a meta,
                    // that's the Invalid Token Error, if there is not, that's some network operation error.
                    val error = converter.toError(response.responseBody!!)
                    callback.onError(InstagramAuthTokenException(error.message))
                } catch (e: Exception) {
                    callback.onError(InstagramAuthNetworkOperationException())
                }
            }
        }
    }
}