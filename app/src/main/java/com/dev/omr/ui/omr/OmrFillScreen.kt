package com.dev.omr.ui.omr

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev.omr.R
import com.dev.omr.data.OmrConfig
import com.dev.omr.data.SessionHolder
import com.dev.omr.data.OmrSession
import com.dev.omr.data.MarkStatus
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OmrFillScreen(config: OmrConfig, onSubmit: (List<Int?>) -> Unit) {
    val answers = remember { mutableStateListOf<Int?>().apply { repeat(config.totalQuestions) { add(null) } } }
    val labels = config.labels()
    var showDialog by remember { mutableStateOf(false) }
    val attempted = answers.count { it != null }

    Scaffold(
        topBar = { TopAppBar(title = { Text("${config.totalQuestions} Q · ${config.optionsPerQuestion} Opt") }) },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(Modifier.padding(12.dp)) {
                    Text(stringResource(R.string.attempted_count, attempted, config.totalQuestions))
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { if (attempted < config.totalQuestions) showDialog = true else submit(config, answers, onSubmit) },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) { Text(stringResource(R.string.submit)) }
                }
            }
        }
    ) { pad ->
        LazyColumn(Modifier.padding(pad).fillMaxSize().padding(horizontal = 12.dp)) {
            itemsIndexed(answers.toList()) { idx, sel ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Q${idx + 1}", modifier = Modifier.width(52.dp), fontWeight = FontWeight.Medium)
                    labels.forEachIndexed { oi, label ->
                        Bubble(selected = sel == oi, label = label) { answers[idx] = if (sel == oi) null else oi }
                    }
                }
            }
        }
    }

    if (showDialog) {
        val unattempted = config.totalQuestions - attempted
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.unattempted_dialog_title)) },
            text = { Text(stringResource(R.string.unattempted_dialog_msg, unattempted)) },
            confirmButton = { TextButton(onClick = { showDialog = false; submit(config, answers, onSubmit) }) { Text(stringResource(R.string.yes_submit)) } },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text(stringResource(R.string.cancel)) } }
        )
    }
}

private fun submit(config: OmrConfig, answers: List<Int?>, onSubmit: (List<Int?>) -> Unit) {
    val s = OmrSession(
        id = UUID.randomUUID().toString(),
        createdAt = System.currentTimeMillis(),
        config = config,
        answers = answers.toList(),
        marks = List(config.totalQuestions) { MarkStatus.PENDING }
    )
    SessionHolder.current = s
    onSubmit(s.answers)
}

@Composable
private fun Bubble(selected: Boolean, label: String, onClick: () -> Unit) {
    Box(
        Modifier
            .padding(end = 10.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}
