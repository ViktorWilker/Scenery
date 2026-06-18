package br.com.scenery.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SoundLayer(
    val id: String,
    val volume: Float,
    @Json(name = "preview_url") val previewUrl: String = ""
)