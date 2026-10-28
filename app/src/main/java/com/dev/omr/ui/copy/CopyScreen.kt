package com.dev.omr.ui.copy

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.dev.omr.R
import com.dev.omr.data.OmrSession
import com.dev.omr.util.ClipUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CopyScreen(session: OmrSession, onCheck: () -> Unit) {
    val ctx = LocalContext.current
    val text = session.copyText()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.copy_for_ai)) }) },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Row(Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { ClipUtils.copy(ctx, text); Toast.makeText(ctx, R.string.copied, Toast.LENGTH_SHORT).show() },
                        modifier = Modifier.weight(1f).height(50.dp)
                    ) { Text(stringResource(R.string.copy)) }
                    OutlinedButton(
                        onClick = { ClipUtils.share(ctx, text) },
                        modifier = Modifier.weight(1f).height(50.dp)
                    ) { Text(stringResource(R.string.share)) }
                }
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize().padding(16.dp)) {
            Card(Modifier.weight(1f).fillMaxWidth()) {
                Column(Modifier.padding(12.dp).verticalScroll(rememberScrollState())) {
                    Text(text, fontFamily = FontFamily.Monospace)
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onCheck,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text(stringResource(R.string.check_answers)) }
        }
    }
}
