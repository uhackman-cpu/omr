package com.dev.omr.ui.check

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev.omr.R
import com.dev.omr.data.MarkStatus
import com.dev.omr.data.OmrSession
import com.dev.omr.data.SessionHolder
import com.dev.omr.ui.theme.GreenRight
import com.dev.omr.ui.theme.RedWrong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckScreen(session: OmrSession, onDone: () -> Unit) {
    val marks = remember { mutableStateListOf<MarkStatus>().apply { addAll(session.marks) } }
    val labels = session.config.labels()
    val r = marks.count { it == MarkStatus.RIGHT }
    val w = marks.count { it == MarkStatus.WRONG }
    val p = marks.count { it == MarkStatus.PENDING }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.check_mode)) },
                actions = {
                    Text("✅$r  ❌$w  ⬜$p", modifier = Modifier.padding(end = 12.dp), fontWeight = FontWeight.Medium)
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(Modifier.padding(12.dp)) {
                    OutlinedButton(
                        onClick = {
                            for (i in marks.indices) if (marks[i] == MarkStatus.PENDING && session.answers[i] == null) marks[i] = MarkStatus.WRONG
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(stringResource(R.string.mark_all_unattempted_wrong)) }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            SessionHolder.current = session.copy(marks = marks.toList())
                            onDone()
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) { Text(stringResource(R.string.see_summary)) }
                }
            }
        }
    ) { pad ->
        LazyColumn(Modifier.padding(pad).fillMaxSize().padding(horizontal = 12.dp)) {
            itemsIndexed(session.answers) { idx, ans ->
                val label = if (ans == null) "-" else labels.getOrNull(ans) ?: "-"
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Q${idx + 1}", modifier = Modifier.width(50.dp), fontWeight = FontWeight.Medium)
                    Text(label, modifier = Modifier.width(40.dp), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { marks[idx] = if (marks[idx] == MarkStatus.RIGHT) MarkStatus.PENDING else MarkStatus.RIGHT }) {
                        Icon(Icons.Default.Check, null, tint = if (marks[idx] == MarkStatus.RIGHT) GreenRight else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = { marks[idx] = if (marks[idx] == MarkStatus.WRONG) MarkStatus.PENDING else MarkStatus.WRONG }) {
                        Icon(Icons.Default.Close, null, tint = if (marks[idx] == MarkStatus.WRONG) RedWrong else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
