package com.nikola.jakshic.instagramauth

internal class HttpParam {

    private val params = HashMap<String, String>()

    fun addParam(key: String, value: String) {
        params[key] = value
    }

    fun getParams() = params
}