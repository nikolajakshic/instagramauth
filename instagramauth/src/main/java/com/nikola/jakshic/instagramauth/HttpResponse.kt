package com.nikola.jakshic.instagramauth

internal class HttpResponse {
    var responseCode: Int = 0
    var responseBody: String? = null
    var error: Exception? = null

    fun isSuccess() = responseCode == 200
}