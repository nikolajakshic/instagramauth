package com.nikola.jakshic.instagramauth

open class InstagramAuthException(message: String) : Exception(message)

private const val DEFAULT_TOKEN_EXCEPTION_MESSAGE = "Invalid access token."
private const val DEFAULT_NOT_INITIALIZED_EXCEPTION_MESSAGE = "InstagramAuth not initialized. InstagramAuth.initialize must be called."
private const val DEFAULT_NOT_LOGGED_IN_EXCEPTION_MESSAGE = "User is not logged in."
private const val DEFAULT_CLIENT_ID_NOT_FOUND_EXCEPTION_MESSAGE = "A valid Instagram CLIENT ID must be set in the AndroidManifest.xml."
private const val DEFAULT_REDIRECT_URI_NOT_FOUND_EXCEPTION_MESSAGE = "A valid Instagram REDIRECT URI must be set in the AndroidManifest.xml."
private const val DEFAULT_ACCESS_DENIED_EXCEPTION_MESSAGE = "The user denied your request."
private const val DEFAULT_NETWORK_OPERATION_EXCEPTION_MESSAGE = "Network operation error."

class InstagramAuthTokenException(message: String = DEFAULT_TOKEN_EXCEPTION_MESSAGE) : InstagramAuthException(message)
class InstagramAuthNotInitializedException(message: String = DEFAULT_NOT_INITIALIZED_EXCEPTION_MESSAGE) : InstagramAuthException(message)
class InstagramAuthNotLoggedInException(message: String = DEFAULT_NOT_LOGGED_IN_EXCEPTION_MESSAGE) : InstagramAuthException(message)
class InstagramAuthInvalidClientIdException(message: String = DEFAULT_CLIENT_ID_NOT_FOUND_EXCEPTION_MESSAGE) : InstagramAuthException(message)
class InstagramAuthInvalidRedirectUriException(message: String = DEFAULT_REDIRECT_URI_NOT_FOUND_EXCEPTION_MESSAGE) : InstagramAuthException(message)
class InstagramAuthAccessDeniedException(message: String = DEFAULT_ACCESS_DENIED_EXCEPTION_MESSAGE) : InstagramAuthException(message)
class InstagramAuthNetworkOperationException(message: String = DEFAULT_NETWORK_OPERATION_EXCEPTION_MESSAGE) : InstagramAuthException(message)