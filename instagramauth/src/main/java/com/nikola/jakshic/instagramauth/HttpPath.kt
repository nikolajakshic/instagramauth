package com.nikola.jakshic.instagramauth

internal class HttpPath {

    private val paths = ArrayList<String>()

    fun addPath(path: String) {
        paths.add(path)
    }

    fun getPaths() = paths
}