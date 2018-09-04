package com.nikola.jakshic.instagramauth

import android.os.NetworkOnMainThreadException
import com.nhaarman.mockitokotlin2.*
import org.junit.Test

class InstagramServiceImplTest {

    private val ACCESS_TOKEN = "fake_access_token"

    @Test
    fun `if http response contains exception - network exception is sent`() {
        val httpClient = mock<HttpClient>()
        val callback = mock<InstagramService.Callback<UserInfo>>()
        val converter = mock<JsonConverter>()
        val service = InstagramServiceImpl(httpClient, converter)

        val response = HttpResponse()
        response.error = InstagramAuthException("exception message")

        whenever(httpClient.request(any(), any())).thenReturn(response)

        service.getUserInfo(ACCESS_TOKEN, callback)

        verify(callback).onError(any())
    }

    @Test
    fun `if http response code eq 200 - on success is called`() {
        val httpClient = mock<HttpClient>()
        val callback = mock<InstagramService.Callback<UserInfo>>()
        val converter = mock<JsonConverter>()
        val service = InstagramServiceImpl(httpClient, converter)

        val response = HttpResponse()
        response.responseCode = 200
        response.responseBody = "fake json data"

        whenever(httpClient.request(any(), any())).thenReturn(response)

        service.getUserInfo(ACCESS_TOKEN, callback)

        verify(callback).onSuccess(anyOrNull())
    }

    @Test
    fun `if http response code not eq 200 and has meta - token exception is sent`() {
        val httpClient = mock<HttpClient>()
        val callback = mock<InstagramService.Callback<UserInfo>>()
        val converter = mock<JsonConverter>()
        val service = InstagramServiceImpl(httpClient, converter)

        val response = HttpResponse()
        response.responseCode = 401
        response.responseBody = "fake json data"

        whenever(converter.toError(any())).thenReturn(Error("",0,""))
        whenever(httpClient.request(any(), any())).thenReturn(response)

        service.getUserInfo(ACCESS_TOKEN, callback)

        verify(callback).onError(any<InstagramAuthTokenException>())
    }

    @Test
    fun `if http response code not eq 200 and has no meta - network exception is sent`() {
        val httpClient = mock<HttpClient>()
        val callback = mock<InstagramService.Callback<UserInfo>>()
        val converter = mock<JsonConverter>()
        val service = InstagramServiceImpl(httpClient, converter)

        val response = HttpResponse()
        response.responseCode = 404
        response.responseBody = "fake json data"

        // throwing exception to simulate failed json conversion attempt, which means that there is no meta
        whenever(converter.toError(any())).thenThrow(RuntimeException())

        whenever(httpClient.request(any(), any())).thenReturn(response)

        service.getUserInfo(ACCESS_TOKEN, callback)

        verify(callback).onError(any<InstagramAuthNetworkOperationException>())
    }

    @Test(expected = NetworkOnMainThreadException::class)
    fun `making requests on the main thread throws exception`() {
        val httpClient = mock<HttpClient>()
        val callback = mock<InstagramService.Callback<UserInfo>>()
        val converter = mock<JsonConverter>()
        val service = InstagramServiceImpl(httpClient, converter)

        val response = HttpResponse()
        response.error = NetworkOnMainThreadException()

        whenever(httpClient.request(any(), any())).thenReturn(response)

        service.getUserInfo(ACCESS_TOKEN, callback)
    }
}