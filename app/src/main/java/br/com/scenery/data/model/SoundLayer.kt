package br.com.scenery.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SoundLayer(
    val id: String,
    val volume: Float
)