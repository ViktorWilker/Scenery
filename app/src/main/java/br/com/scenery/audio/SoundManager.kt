package br.com.scenery.audio

import android.media.MediaPlayer
import br.com.scenery.data.model.SoundLayer

class SoundManager {

    private val activePlayers = mutableMapOf<String, MediaPlayer>()

    fun play(layers: List<SoundLayer>) {
        stopAll()
        layers.forEach { layer ->
            android.util.Log.d("SoundManager", "Playing: ${layer.id} -> ${layer.previewUrl}")
            if (layer.previewUrl.isBlank()) {
                android.util.Log.e("SoundManager", "Empty URL for ${layer.id}, skipping")
                return@forEach
            }
            val player = MediaPlayer().apply {
                setDataSource(layer.previewUrl)
                setOnPreparedListener {
                    setVolume(layer.volume, layer.volume)
                    isLooping = true
                    start()
                }
                prepareAsync()
            }
            activePlayers[layer.id] = player
        }
    }


    fun setVolume(id: String, volume: Float) {
        activePlayers[id]?.setVolume(volume, volume)
    }

    fun pause() {
        activePlayers.values.forEach { if (it.isPlaying) it.pause() }
    }

    fun resume() {
        activePlayers.values.forEach { if (!it.isPlaying) it.start() }
    }

    fun stopAll() {
        activePlayers.values.forEach {
            try {
                it.stop()
            } catch (e: IllegalStateException) {
            } finally {
                it.release()
            }
        }
        activePlayers.clear()
    }


    fun release() {
        stopAll()
    }
}