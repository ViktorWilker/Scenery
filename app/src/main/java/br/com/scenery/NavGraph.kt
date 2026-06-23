package br.com.scenery

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.scenery.ui.screen.PromptScreen
import br.com.scenery.ui.screen.SceneScreen
import br.com.scenery.ui.screen.SplashScreen
import br.com.scenery.viewmodel.SceneViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Prompt : Screen("prompt")
    object Scene : Screen("scene")
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: SceneViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val timerSeconds by viewModel.timerSeconds.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.fillMaxSize().systemBarsPadding()
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Screen.Prompt.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Prompt.route) {
            PromptScreen(
                viewModel = viewModel,
                uiState = uiState,
                onSceneReady = {
                    navController.navigate(Screen.Scene.route)
                }
            )
        }

        composable(Screen.Scene.route) {
            val activeScene = (uiState as? SceneViewModel.UiState.SceneActive)?.scene
                ?: return@composable

            SceneScreen(
                scene = activeScene,
                isPlaying = isPlaying,
                timerSeconds = timerSeconds,
                onTogglePlayback = viewModel::togglePlayback,
                onSetVolume = viewModel::setVolume,
                onStartTimer = viewModel::startTimer,
                onBack = {
                    viewModel.resetScene()
                    navController.popBackStack()
                }
            )
        }
    }
}