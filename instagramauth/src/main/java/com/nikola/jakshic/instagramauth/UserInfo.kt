package com.nikola.jakshic.instagramauth

class UserInfo(
        val id: String,
        val userName: String,
        val fullName: String,
        val photoUrl: String,
        val bio: String,
        val website: String,
        val isBusiness: Boolean,
        val counts: Counts) {

    class Counts(val media: Int, val follows: Int, val followedBy: Int)
}