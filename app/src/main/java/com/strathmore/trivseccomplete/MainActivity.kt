package com.strathmore.trivseccomplete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.strathmore.trivseccomplete.ui.theme.TrivSecCompleteTheme


import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import com.strathmore.trivseccomplete.Routes.Quiz
import com.strathmore.trivseccomplete.ui.theme.TrivSecCompleteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrivSecCompleteTheme  {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.Home) {
                    composable(Routes.Home) { Home(navController) }
                    composable(Routes.Quiz) { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        Quiz(navController, username)
                    }
                    composable(Routes.Score) { Score(navController) }
                }
            }
        }
    }
}

object Routes {
    const val Home = "Home"
    const val Quiz = "Quiz?username={username}"
    const val Score = "Score"
}

@Composable
fun Home(navController: NavController) {
    var username by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.intro),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.7f),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.applogo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 32.dp)
            )

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Your Name", color = Color.White) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = { navController.navigate("Quiz?username=${username.trim()}") },
                enabled = username.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4285F4),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Start Quiz")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Quiz(navController: NavController, username: String) {
    var questions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var currentIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    val ChosenAnswers: MutableList<ChosenAnswers> = remember { mutableStateListOf() }
    var showReviewMode by remember { mutableStateOf(false) }

    val database = Firebase.database
    val ref = database.getReference("questions")

    LaunchedEffect(Unit) {
        ref.get().addOnSuccessListener { dataSnapshot ->
            val fetched = mutableListOf<Question>()
            dataSnapshot.children.forEach { item ->
                val question = item.getValue(Question::class.java)
                question?.let { fetched.add(it) }
            }
            questions = fetched.shuffled()
        }.addOnFailureListener { exception ->
            println("Error fetching questions: ${exception.message}")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.quizbackground),
            contentDescription = "Quiz Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.7f)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (showReviewMode) "Review Answers" else "Hello $username!",
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    if (questions.isEmpty()) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth().testTag("Progress Indicator"),
                            color = Color.White
                        )
                        Text(
                            "Loading questions...",
                            modifier = Modifier.padding(top = 8.dp),
                            color = Color.White
                        )
                    } else if (showReviewMode) {
                        ReviewQuestions(userAnswersResults = ChosenAnswers)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate(Routes.Home) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4285F4),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Finish Quiz")
                        }
                    } else if (currentIndex < questions.size) {
                        val q = questions[currentIndex]

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Question ${currentIndex + 1} of ${questions.size}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    q.question,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        q.choices.forEach { (key, value) ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Color.Black.copy(alpha = 0.3f))
                                    .padding(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    RadioButton(
                                        selected = selectedOption == key,
                                        onClick = { selectedOption = key },
                                        enabled = true,
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color(0xFF4285F4),
                                            unselectedColor = Color.White
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(value, color = Color.White)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val isCurrentCorrect = (selectedOption == q.correctAnswer)
                                if (isCurrentCorrect) score++

                                ChosenAnswers.add(
                                    ChosenAnswers(
                                        question = q,
                                        ChosenAnswersKey = selectedOption,
                                        isCorrect = isCurrentCorrect
                                    )
                                )

                                currentIndex++
                                selectedOption = ""
                            },
                            enabled = selectedOption.isNotBlank(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4285F4),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Next")
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    "Quiz Finished!",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Your Score: $score / ${questions.size}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                if (score == questions.size) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.completion),
                                            contentDescription = "Perfect Score Award",
                                            modifier = Modifier
                                                .fillMaxWidth(0.8f)
                                                .padding(16.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showReviewMode = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4285F4),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Review Answers")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { navController.navigate(Routes.Home) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text("Go to Home")
                        }
                    }
                }
            }
        )
    }
}



@Composable
fun ReviewQuestions(userAnswersResults: List<ChosenAnswers>) {
    Column {
        Text(
            "Detailed Review",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color.White
        )
        if (userAnswersResults.isEmpty()) {
            Text("No questions to review.", color = Color.White)
        } else {
            LazyColumn {
                items(userAnswersResults) { result ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = result.question.question,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            result.question.choices.forEach { (key, value) ->
                                val optionColor = when {
                                    key == result.question.correctAnswer -> Color.Green.copy(alpha = 0.7f)
                                    key == result.ChosenAnswersKey && !result.isCorrect -> Color.Red.copy(alpha = 0.7f)
                                    else -> Color.Transparent
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(optionColor)
                                        .padding(4.dp)
                                ) {
                                    val isSelected = key == result.ChosenAnswersKey
                                    val isCorrectOption = key == result.question.correctAnswer

                                    Text(
                                        text = value,
                                        color = if (isSelected || isCorrectOption) Color.White else Color.Black,
                                        fontWeight = if (isCorrectOption) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    if (isSelected) {
                                        Icon(
                                            imageVector = if (result.isCorrect) Icons.Default.Check else Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (result.isCorrect) Color.White else Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Score(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Score Page", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Quiz Completed!", fontSize = 18.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(Routes.Home) }) {
            Text("Go to Home")
        }
    }
}
