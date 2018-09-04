package com.nikola.jakshic.instagramauth

import android.net.Uri
import java.io.BufferedReader
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

internal class HttpClient(private val BASE_URL: String) {

    fun request(paths: HttpPath, params: HttpParam): HttpResponse {
        var connection: HttpsURLConnection? = null
        var stream: InputStream? = null
        var reader: BufferedReader? = null
        val response = HttpResponse()
        val stringUrl = buildUrl(paths, params)

        try {
            val url = URL(stringUrl)
            connection = url.openConnection() as HttpsURLConnection
            connection.readTimeout = 10000
            connection.connectTimeout = 10000

            val responseCode = connection.responseCode
            stream = if (responseCode == HttpsURLConnection.HTTP_OK) connection.inputStream
            else connection.errorStream

            reader = stream?.bufferedReader()
            val body = reader?.readText()

            response.responseBody = body
            response.responseCode = responseCode
        } catch (e: Exception) {
            response.error = e
        } finally {
            stream?.close()
            connection?.disconnect()
            reader?.close()
        }
        return response
    }

    private fun buildUrl(paths: HttpPath, params: HttpParam): String {
        val uri = Uri.parse(BASE_URL).buildUpon()

        for (path in paths.getPaths()) uri.appendPath(path)
        for ((key, value) in params.getParams()) uri.appendQueryParameter(key, value)

        return uri.toString()
    }
}