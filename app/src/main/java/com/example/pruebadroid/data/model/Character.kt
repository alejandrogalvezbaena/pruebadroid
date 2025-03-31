package com.example.pruebadroid.data.model

data class Character(
    val id: Int, val name: String, val species: String, val image: String,
    val status: String
) {
    companion object {
        const val STATUS_ALIVE = "Alive"
        const val STATUS_DEAD = "Dead"
        const val STATUS_UNKNOWN = "unknown"
    }
}