package com.nikola.jakshic.instagramauth

import org.json.JSONObject

// Marked as a object for testing purposes, can't mock companion object
internal object JsonConverter {

    fun toUserInfo(jsonString: String): UserInfo {
        val jsonObject = JSONObject(jsonString)

        val dataObject = jsonObject.getJSONObject("data")

        val id = dataObject.getString("id")
        val userName = dataObject.getString("username")
        val photoUrl = dataObject.getString("profile_picture")
        val fullName = dataObject.getString("full_name")
        val bio = dataObject.getString("bio")
        val website = dataObject.getString("website")
        val isBusiness = dataObject.getBoolean("is_business")

        val countsObject = dataObject.getJSONObject("counts")

        val media = countsObject.getInt("media")
        val follows = countsObject.getInt("follows")
        val followedBy = countsObject.getInt("followed_by")

        val counts = UserInfo.Counts(media, follows, followedBy)

        return UserInfo(id, userName, fullName, photoUrl, bio, website, isBusiness, counts)
    }

    fun toError(jsonString: String): Error {
        val jsonObject = JSONObject(jsonString)

        val metaObject = jsonObject.getJSONObject("meta")

        val code = metaObject.getInt("code")
        val type = metaObject.getString("error_type")
        val message = metaObject.getString("error_message")

        return Error(type, code, message)
    }
}