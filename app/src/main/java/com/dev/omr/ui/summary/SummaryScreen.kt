package com.dev.omr.ui.summary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dev.omr.R
import com.dev.omr.data.MarkStatus
import com.dev.omr.data.OmrSession
import com.dev.omr.ui.theme.GreenRight
import com.dev.omr.ui.theme.RedWrong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(session: OmrSession, onNewOmr: () -> Unit, onReview: () -> Unit) {
    val total = session.config.totalQuestions
    val r = session.marks.count { it == MarkStatus.RIGHT }
    val w = session.marks.count { it == MarkStatus.WRONG }
    val un = session.answers.count { it == null }
    val pct = if (total == 0) 0 else r * 100 / total
    val labels = session.config.labels()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.summary)) }) }
    ) { pad ->
        Column(
            Modifier.padding(pad).fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.score), style = MaterialTheme.typography.titleMedium)
                    Text("$r / $total", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                    Text("$pct%", style = MaterialTheme.typography.titleLarge)
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Stat(stringResource(R.string.correct), r, GreenRight)
                Stat(stringResource(R.string.wrong_label), w, RedWrong)
                Stat(stringResource(R.string.unattempted), un, MaterialTheme.colorScheme.onSurfaceVariant)
            }

            if (w > 0) {
                Text(stringResource(R.string.wrong_questions), style = MaterialTheme.typography.titleMedium)
                val wrongNums = session.marks.indices.filter { session.marks[it] == MarkStatus.WRONG }
                val txt = wrongNums.joinToString(", ") { "Q${it + 1} (${labels.getOrNull(session.answers[it] ?: -1) ?: "-"})" }
                Text(txt)
            }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onReview, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.review_again))
            }
            Button(onClick = onNewOmr, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text(stringResource(R.string.new_omr))
            }
        }
    }
}

@Composable
private fun Stat(label: String, value: Int, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text("$value", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = color)
    }
}
