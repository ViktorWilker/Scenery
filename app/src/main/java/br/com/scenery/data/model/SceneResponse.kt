package br.com.scenery.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SceneResponse(
    @Json(name = "scene_name") val sceneName: String,
    @Json(name = "mood") val mood: String,
    @Json(name = "sounds") val sounds: List<SoundLayer>
)