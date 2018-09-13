package com.nikola.jakshic.instagramauth

import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri

class InstagramAuthInitProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        InstagramAuth.initialize(context!!)
        return false
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?) = null

    override fun insert(uri: Uri?, values: ContentValues?) = null

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?) = 0

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?) = 0

    override fun getType(uri: Uri?) = null
}