package com.nikola.jakshic.instagramauth

import android.content.Context

class InstagramAuth {

    companion object {

        @JvmStatic
        @Deprecated("InstagramAuth is automatically initialized at app startup time.")
        fun initialize(context: Context) {
            AuthManager.initialize(context)
        }
    }
}