package com.strathmore.trivseccomplete

data class Question(
    val question: String = "",
    val choices: Map<String, String> = mapOf(),
    val correctAnswer: String = ""
)
