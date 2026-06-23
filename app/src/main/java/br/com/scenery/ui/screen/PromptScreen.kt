package br.com.scenery.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.scenery.ui.theme.*
import br.com.scenery.viewmodel.SceneViewModel

private val suggestions = listOf(
    "floresta à noite",
    "taverna medieval",
    "nave espacial",
    "praia ao amanhecer",
    "café parisiense",
    "biblioteca vazia"
)

@Composable
fun PromptScreen(
    viewModel: SceneViewModel,
    uiState: SceneViewModel.UiState,
    onSceneReady: () -> Unit
) {
    LaunchedEffect(uiState) {
        if (uiState is SceneViewModel.UiState.SceneActive) {
            onSceneReady()
        }
    }

    var prompt by remember { mutableStateOf("") }
    val isLoading = uiState is SceneViewModel.UiState.Loading
    val error = (uiState as? SceneViewModel.UiState.Error)?.message

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val labelAlpha by animateFloatAsState(targetValue = if (visible) 1f else 0f, animationSpec = tween(400, 0), label = "")
    val labelOffset by animateFloatAsState(targetValue = if (visible) 0f else 16f, animationSpec = tween(400, 0), label = "")

    val fieldAlpha by animateFloatAsState(targetValue = if (visible) 1f else 0f, animationSpec = tween(400, 260), label = "")
    val fieldOffset by animateFloatAsState(targetValue = if (visible) 0f else 16f, animationSpec = tween(400, 260), label = "")

    val chipsAlpha by animateFloatAsState(targetValue = if (visible) 1f else 0f, animationSpec = tween(400, 340), label = "")
    val chipsOffset by animateFloatAsState(targetValue = if (visible) 0f else 16f, animationSpec = tween(400, 340), label = "")

    val buttonAlpha by animateFloatAsState(targetValue = if (visible) 1f else 0f, animationSpec = tween(400, 420), label = "")
    val buttonOffset by animateFloatAsState(targetValue = if (visible) 0f else 16f, animationSpec = tween(400, 420), label = "")

    val titleWords = "Descreva onde você quer estar".split(" ")
    val subtitleWords = "e a cena vai tocar pra você".split(" ")
    val allWords = titleWords + subtitleWords

    val wordAlphas = allWords.mapIndexed { index, _ ->
        animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = tween(300, delayMillis = 80 + index * 60),
            label = "word_$index"
        ).value
    }
    val wordOffsets = allWords.mapIndexed { index, _ ->
        animateFloatAsState(
            targetValue = if (visible) 0f else 12f,
            animationSpec = tween(300, delayMillis = 80 + index * 60),
            label = "offset_$index"
        ).value
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "SCENERY",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            letterSpacing = 2.sp,
            modifier = Modifier
                .alpha(labelAlpha)
                .offset(y = labelOffset.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        WordByWordText(
            words = titleWords,
            alphas = wordAlphas.take(titleWords.size),
            offsets = wordOffsets.take(titleWords.size),
            style = MaterialTheme.typography.displayMedium,
            color = TextPrimary
        )

        WordByWordText(
            words = subtitleWords,
            alphas = wordAlphas.drop(titleWords.size),
            offsets = wordOffsets.drop(titleWords.size),
            style = MaterialTheme.typography.displayMedium.copy(
                fontStyle = FontStyle.Italic,
                color = TextSecondary
            ),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = prompt,
            onValueChange = { if (it.length <= 200) prompt = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .alpha(fieldAlpha)
                .offset(y = fieldOffset.dp),
            placeholder = {
                Text(
                    "Ex: cabine de trem cruzando a Sibéria de noite...",
                    color = Color(0xFFC8C6C0),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextPrimary),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TextPrimary,
                unfocusedBorderColor = Border,
                focusedContainerColor = Surface,
                unfocusedContainerColor = Surface
            ),
            supportingText = {
                Text(
                    text = "${prompt.length} / 200",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .alpha(chipsAlpha)
                .offset(y = chipsOffset.dp)
        ) {
            items(suggestions) { suggestion ->
                SuggestionChip(
                    onClick = { prompt = suggestion },
                    label = {
                        Text(
                            suggestion,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = Border
                    ),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = Surface
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = { if (prompt.isNotBlank()) viewModel.generateScene(prompt) },
            enabled = prompt.isNotBlank() && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .alpha(buttonAlpha)
                .offset(y = buttonOffset.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TextPrimary,
                contentColor = Surface,
                disabledContainerColor = Border,
                disabledContentColor = TextSecondary
            )
        ) {
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                },
                label = "button_content"
            ) { loading ->
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Surface,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Criar cena →", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WordByWordText(
    words: List<String>,
    alphas: List<Float>,
    offsets: List<Float>,
    style: TextStyle,
    color: androidx.compose.ui.graphics.Color
) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        var layoutWidth by remember { mutableStateOf(0) }
        androidx.compose.ui.layout.Layout(
            content = {
                words.forEachIndexed { index, word ->
                    Text(
                        text = word,
                        style = style,
                        color = color,
                        modifier = Modifier
                            .alpha(alphas[index])
                            .offset(y = offsets[index].dp)
                    )
                }
            }
        ) { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0)) }
            val gap = 6.dp.roundToPx()
            var x = 0
            var y = 0
            var rowHeight = 0

            val positions = placeables.map { placeable ->
                if (x + placeable.width > constraints.maxWidth && x > 0) {
                    x = 0
                    y += rowHeight + gap
                    rowHeight = 0
                }
                val pos = Pair(x, y)
                x += placeable.width + gap
                rowHeight = maxOf(rowHeight, placeable.height)
                pos
            }

            val totalHeight = y + rowHeight
            layout(constraints.maxWidth, totalHeight) {
                placeables.forEachIndexed { i, placeable ->
                    placeable.placeRelative(positions[i].first, positions[i].second)
                }
            }
        }
    }
}