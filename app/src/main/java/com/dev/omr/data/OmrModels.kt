package com.dev.omr.data

import kotlinx.serialization.Serializable

@Serializable
enum class OptionLabel { ABCD, NUMBERS }

@Serializable
enum class MarkStatus { PENDING, RIGHT, WRONG }

@Serializable
data class OmrConfig(
    val totalQuestions: Int = 10,
    val optionsPerQuestion: Int = 4,
    val labelStyle: OptionLabel = OptionLabel.ABCD
) {
    fun labels(): List<String> = when (labelStyle) {
        OptionLabel.ABCD -> listOf("A","B","C","D","E").take(optionsPerQuestion)
        OptionLabel.NUMBERS -> listOf("1","2","3","4","5").take(optionsPerQuestion)
    }
}

@Serializable
data class OmrSession(
    val id: String,
    val createdAt: Long,
    val config: OmrConfig,
    val answers: List<Int?>,
    val marks: List<MarkStatus>
) {
    fun attemptedCount(): Int = answers.count { it != null }
    fun rightCount(): Int = marks.count { it == MarkStatus.RIGHT }
    fun wrongCount(): Int = marks.count { it == MarkStatus.WRONG }
    fun unattemptedCount(): Int = answers.count { it == null }
    fun pendingCount(): Int = marks.count { it == MarkStatus.PENDING }
    fun score(): Int = rightCount()
    fun percentage(): Int = if (config.totalQuestions == 0) 0
        else (rightCount() * 100) / config.totalQuestions

    fun copyText(): String {
        val labels = config.labels()
        val style = if (config.labelStyle == OptionLabel.ABCD) "ABCDE" else "12345"
        val sb = StringBuilder()
        sb.appendLine("OMR - ${config.totalQuestions} Questions | Options: $style")
        answers.forEachIndexed { i, ans ->
            val v = if (ans == null) "-" else labels.getOrNull(ans) ?: "-"
            sb.appendLine("Q${i + 1}: $v")
        }
        val un = answers.indices.filter { answers[it] == null }.map { "Q${it + 1}" }
        if (un.isNotEmpty()) sb.appendLine("Unattempted: ${un.joinToString(", ")}")
        return sb.toString().trim()
    }
}

object SessionHolder {
    var current: OmrSession? = null
}
