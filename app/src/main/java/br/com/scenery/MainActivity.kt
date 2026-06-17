package br.com.scenery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.scenery.ui.screen.PromptScreen
import br.com.scenery.ui.screen.SceneScreen
import br.com.scenery.ui.theme.SceneryTheme
import br.com.scenery.viewmodel.SceneViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SceneryTheme {
                val viewModel: SceneViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                val isPlaying by viewModel.isPlaying.collectAsState()
                val timerSeconds by viewModel.timerSeconds.collectAsState()

                val activeScene = (uiState as? SceneViewModel.UiState.SceneActive)?.scene

                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .safeDrawingPadding()
                ) {
                    if (activeScene != null) {
                        SceneScreen(
                            scene = activeScene,
                            isPlaying = isPlaying,
                            timerSeconds = timerSeconds,
                            onTogglePlayback = viewModel::togglePlayback,
                            onSetVolume = viewModel::setVolume,
                            onStartTimer = viewModel::startTimer,
                            onBack = viewModel::resetScene
                        )
                    } else {
                        PromptScreen(
                            viewModel = viewModel,
                            uiState = uiState
                        )
                    }
                }
            }
        }
    }
}