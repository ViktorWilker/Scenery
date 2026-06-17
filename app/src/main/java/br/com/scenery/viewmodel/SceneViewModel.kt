package br.com.scenery.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.scenery.audio.SoundManager
import br.com.scenery.data.model.SceneResponse
import br.com.scenery.data.remote.GeminiRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SceneViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GeminiRepository()
    private val soundManager = SoundManager(application)

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class SceneActive(val scene: SceneResponse) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _timerSeconds = MutableStateFlow(0)
    val timerSeconds: StateFlow<Int> = _timerSeconds

    private var timerJob: Job? = null

    fun generateScene(prompt: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.generateScene(prompt)
                .onSuccess { scene ->
                    soundManager.play(scene.sounds)
                    _isPlaying.value = true
                    _uiState.value = UiState.SceneActive(scene)
                }
                .onFailure { e ->
                    _uiState.value = UiState.Error(e.message ?: "Erro desconhecido")
                }
        }
    }

    fun togglePlayback() {
        if (_isPlaying.value) {
            soundManager.pause()
        } else {
            soundManager.resume()
        }
        _isPlaying.value = !_isPlaying.value
    }

    fun setVolume(id: String, volume: Float) {
        soundManager.setVolume(id, volume)
    }

    fun startTimer(minutes: Int) {
        timerJob?.cancel()
        if (minutes == 0) {
            _timerSeconds.value = 0
            return
        }
        _timerSeconds.value = minutes * 60
        timerJob = viewModelScope.launch {
            while (_timerSeconds.value > 0) {
                delay(1000)
                _timerSeconds.value -= 1
            }
            soundManager.stopAll()
            _isPlaying.value = false
        }
    }

    fun resetScene() {
        timerJob?.cancel()
        soundManager.stopAll()
        _uiState.value = UiState.Idle
        _timerSeconds.value = 0
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundManager.release()
    }
}