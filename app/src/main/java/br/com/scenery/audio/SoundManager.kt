package br.com.scenery.audio

import android.content.Context
import android.media.MediaPlayer
import br.com.scenery.data.local.SoundAssets
import br.com.scenery.data.model.SoundLayer

class SoundManager(context: Context) {

    private val ctx = context.applicationContext
    private val activePlayers = mutableMapOf<String, MediaPlayer>()

    fun play(layers: List<SoundLayer>) {
        stopAll()
        layers.forEach { layer ->
            val resId = SoundAssets.map[layer.id] ?: return@forEach
            val player = MediaPlayer.create(ctx, resId).apply {
                isLooping = true
                setVolume(layer.volume, layer.volume)
                start()
            }
            activePlayers[layer.id] = player
        }
    }

    fun setVolume(id: String, volume: Float) {
        activePlayers[id]?.setVolume(volume, volume)
    }

    fun pause() {
        activePlayers.values.forEach { it.pause() }
    }

    fun resume() {
        activePlayers.values.forEach { it.start() }
    }

    fun stopAll() {
        activePlayers.values.forEach {
            it.stop()
            it.release()
        }
        activePlayers.clear()
    }

    fun release() {
        stopAll()
    }
}