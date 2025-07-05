package com.strathmore.trivseccomplete

import junit.framework.TestCase.assertTrue
import  org.junit.Assert
import org.junit.Test
import kotlin.test.assertFalse

class QuizLogicTest {
    @Test
    fun testCorrectAnswerSelection(){
        val question = Question(
            question = "What is phishing?",
            correctAnswer = "C",
            choices = mapOf("A" to "Fake emails", "B" to "Firewall", "C" to "Malicious software", "D" to "SNMP")
        )
        val selectedOption = "C"
        val isAnswerCorrect = selectedOption == question.correctAnswer
        assertTrue ( isAnswerCorrect)
    }
    @Test
    fun testIncorrectAnswerSelection(){
        val question = Question(
            question = "What is phishing?",
            correctAnswer = "C",
            choices = mapOf("A" to "Fake emails", "B" to "Firewall", "C" to "Malicious software", "D" to "SNMP")
        )
        val selectedOption = "B"
        val isAnswerCorrect = selectedOption == question.correctAnswer
        assertFalse( isAnswerCorrect)
}}