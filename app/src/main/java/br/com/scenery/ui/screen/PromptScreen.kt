package br.com.scenery.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    uiState: SceneViewModel.UiState
) {
    var prompt by remember { mutableStateOf("") }
    val isLoading = uiState is SceneViewModel.UiState.Loading
    val error = (uiState as? SceneViewModel.UiState.Error)?.message

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
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Descreva onde\nvocê quer estar",
            style = MaterialTheme.typography.displayMedium,
            color = TextPrimary
        )

        Text(
            text = "e a cena vai tocar pra você",
            style = MaterialTheme.typography.displayMedium.copy(
                fontStyle = FontStyle.Italic,
                color = TextSecondary
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = prompt,
            onValueChange = { if (it.length <= 200) prompt = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
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

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TextPrimary,
                contentColor = Surface,
                disabledContainerColor = Border,
                disabledContentColor = TextSecondary
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Surface,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Criar cena →", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}