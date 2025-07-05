package com.strathmore.trivseccomplete

data class ChosenAnswers(
    val question: Question,
    val ChosenAnswersKey: String, // The key of the option the user selected (e.g., "a", "b")
    val isCorrect: Boolean
)
