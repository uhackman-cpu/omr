package com.dev.omr.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dev.omr.R
import com.dev.omr.data.OmrConfig
import com.dev.omr.data.OptionLabel
import com.dev.omr.util.LocaleHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onStart: (OmrConfig) -> Unit) {
    val ctx = LocalContext.current
    var total by remember { mutableStateOf("10") }
    var options by remember { mutableStateOf(4) }
    var label by remember { mutableStateOf(OptionLabel.ABCD) }
    var lang by remember { mutableStateOf(LocaleHelper.current(ctx)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    TextButton(onClick = {
                        val newLang = if (lang == "en") "hi" else "en"
                        lang = newLang
                        LocaleHelper.set(ctx, newLang)
                    }) {
                        Text(if (lang == "en") "हिन्दी" else "English")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier.padding(pad).fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(stringResource(R.string.create_omr), style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = total,
                onValueChange = { v -> if (v.length <= 3 && v.all { it.isDigit() }) total = v },
                label = { Text(stringResource(R.string.total_questions)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Text(stringResource(R.string.options_per_question), style = MaterialTheme.typography.titleMedium)
            Row(Modifier.selectableGroup()) {
                listOf(4, 5).forEach { n ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 16.dp)) {
                        RadioButton(selected = options == n, onClick = { options = n })
                        Text("$n")
                    }
                }
            }

            Text(stringResource(R.string.label_style), style = MaterialTheme.typography.titleMedium)
            Row(Modifier.selectableGroup()) {
                listOf(OptionLabel.ABCD, OptionLabel.NUMBERS).forEach { l ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 16.dp)) {
                        RadioButton(selected = label == l, onClick = { label = l })
                        Text(if (l == OptionLabel.ABCD) "ABCDE" else "12345")
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            val q = total.toIntOrNull() ?: 0
            Button(
                onClick = { onStart(OmrConfig(q, options, label)) },
                enabled = q in 5..200,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text(stringResource(R.string.start_omr)) }
        }
    }
}
