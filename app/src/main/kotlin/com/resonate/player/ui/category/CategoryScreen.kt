package com.resonate.player.ui.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.resonate.player.R
import com.resonate.player.ui.theme.ResonateTheme

@Composable
fun CategoryScreen(
    category: String,
    onBack: () -> Unit,
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

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint = ResonateTheme.colors.bone,
                )
            }
            Text(
                text = stringResource(titleRes),
                style = ResonateTheme.type.title,
                color = ResonateTheme.colors.bone,
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            color = ResonateTheme.colors.surfaceRaised,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(titleRes),
                    style = ResonateTheme.type.displaySm,
                    color = ResonateTheme.colors.bone,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(hintRes),
                    style = ResonateTheme.type.body,
                    color = ResonateTheme.colors.muted,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(R.string.category_empty_title),
                    style = ResonateTheme.type.title,
                    color = ResonateTheme.colors.accent,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 20.dp),
                )
                Text(
                    text = stringResource(R.string.category_empty_body),
                    style = ResonateTheme.type.body,
                    color = ResonateTheme.colors.muted,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
