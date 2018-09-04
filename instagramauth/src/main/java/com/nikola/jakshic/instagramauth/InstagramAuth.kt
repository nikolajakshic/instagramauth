package com.nikola.jakshic.instagramauth

import android.content.Context

class InstagramAuth {

    companion object {

        @JvmStatic
        fun initialize(context: Context) {
            AuthManager.initialize(context)
        }
    }
}