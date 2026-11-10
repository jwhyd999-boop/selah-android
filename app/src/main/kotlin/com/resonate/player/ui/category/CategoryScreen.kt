package com.resonate.player.ui.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.resonate.player.R
import com.resonate.player.data.catalog.CatalogAudioItem
import com.resonate.player.ui.theme.ResonateTheme

@Composable
fun CategoryScreen(
    category: String,
    onBack: () -> Unit,
    onPlay: (CatalogAudioItem) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    val titleRes = when (category) {
        "sermons" -> R.string.category_sermons
        "hymns" -> R.string.category_hymns
        "psalms" -> R.string.category_psalms
        "agpeya" -> R.string.category_agpeya
        else -> R.string.category_sermons
    }
    val hintRes = when (category) {
        "sermons" -> R.string.category_sermons_hint
        "hymns" -> R.string.category_hymns_hint
        "psalms" -> R.string.category_psalms_hint
        "agpeya" -> R.string.category_agpeya_hint
        else -> R.string.category_sermons_hint
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(category) { viewModel.load(category) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back), tint = ResonateTheme.colors.bone)
            }
            Text(text = stringResource(titleRes), style = ResonateTheme.type.title, color = ResonateTheme.colors.bone)
        }
        Text(
            text = stringResource(hintRes),
            style = ResonateTheme.type.body,
            color = ResonateTheme.colors.muted,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        )

        when {
            state.loading -> Column(
                modifier = Modifier.fillMaxWidth().padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CircularProgressIndicator(color = ResonateTheme.colors.accent)
                Text(stringResource(R.string.catalog_loading), color = ResonateTheme.colors.muted)
            }
            state.error != null -> Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                val notConfigured = state.error?.message?.contains("not configured", ignoreCase = true) == true
                Text(
                    text = stringResource(if (notConfigured) R.string.catalog_not_configured else R.string.catalog_error),
                    color = ResonateTheme.colors.muted,
                    textAlign = TextAlign.Center,
                )
                TextButton(onClick = { viewModel.load(category) }) {
                    Text(stringResource(R.string.catalog_retry), color = ResonateTheme.colors.accent)
                }
            }
            state.items.isEmpty() -> Text(
                text = stringResource(R.string.catalog_empty),
                color = ResonateTheme.colors.muted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(32.dp),
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(state.items, key = { it.id }) { item ->
                    Surface(
                        modifier = Modifier.fillMaxWidth().clickable { onPlay(item) },
                        color = ResonateTheme.colors.surfaceRaised,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.titleAr, style = ResonateTheme.type.title, color = ResonateTheme.colors.bone)
                                item.speakerOrPerformer?.takeIf(String::isNotBlank)?.let {
                                    Text(it, style = ResonateTheme.type.body, color = ResonateTheme.colors.muted)
                                }
                            }
                            Icon(Icons.Default.PlayArrow, contentDescription = stringResource(R.string.catalog_play), tint = ResonateTheme.colors.accent)
                        }
                    }
                }
            }
        }
    }
}
