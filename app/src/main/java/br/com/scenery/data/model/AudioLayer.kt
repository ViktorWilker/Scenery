package br.com.scenery.data.model

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class AudioLayer(
    context: Context,
    url: String,
    volume: Float
) {

    private val player =
        ExoPlayer.Builder(context)
            .build()

    init {

        player.setMediaItem(
            MediaItem.fromUri(url)
        )

        player.repeatMode =
            Player.REPEAT_MODE_ONE

        player.volume =
            volume

        player.prepare()
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun resume() {
        player.play()
    }

    fun setVolume(
        volume: Float
    ) {
        player.volume =
            volume
    }

    fun release() {
        player.release()
    }
}