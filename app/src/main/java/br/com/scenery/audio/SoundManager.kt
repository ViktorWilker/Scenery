package br.com.scenery.audio

import android.content.Context
import android.media.MediaPlayer
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.Player
import br.com.scenery.data.model.AudioLayer
import br.com.scenery.data.model.SoundLayer

class SoundManager(
    private val context: Context
) {

    private val layers =
        mutableMapOf<String, AudioLayer>()

    fun play(
        sounds: List<SoundLayer>
    ) {

        stopAll()

        sounds.forEach {

            if (
                it.previewUrl.isBlank()
            ) return@forEach

            val layer =
                AudioLayer(
                    context,
                    it.previewUrl,
                    it.volume
                )

            layer.play()

            layers[it.id] =
                layer
        }
    }

    fun setVolume(
        id: String,
        volume: Float
    ) {
        layers[id]
            ?.setVolume(volume)
    }

    fun pause() {

        layers.values
            .forEach {
                it.pause()
            }
    }

    fun resume() {

        layers.values
            .forEach {
                it.resume()
            }
    }

    fun stopAll() {

        layers.values
            .forEach {
                it.release()
            }

        layers.clear()
    }

    fun release() {
        stopAll()
    }
}