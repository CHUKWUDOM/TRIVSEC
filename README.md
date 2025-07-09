# **TrivSec- CyberSecurity Trivia Quiz App** 
## **Overview** 
TrivSec is an Android-based trivia quiz application built using Jetpack Compose and Firebase Realtime Database. The app allows users to enter their name, take a quiz with questions fetched from Firebase, track their score, review their answers, and navigate back to the home screen. It features a modern, user-friendly interface with a dark theme and randomized question order for an engaging experience.

## **Features** 
* Home Screen: Users enter their name to start the quiz.
* Quiz Screen: Displays multiple-choice questions fetched from Firebase Realtime Database, tracks user answers, and calculates the score.
* Review Mode: Shows a detailed review of the user’s answers, highlighting correct and incorrect choices.
* Score Display: Presents the final score and a special award for a perfect score.
* Navigation: Seamless navigation between Home, Quiz, and Score screens using Jetpack Navigation.
* Randomized Questions: Questions are shuffled for each quiz session to enhance repeatability.

# **Tech Stack** 
* Frontend: Jetpack Compose (Android’s modern UI toolkit)
* Backend: Firebase Realtime Database (for storing and fetching quiz questions)
* Language: Kotlin
* Navigation: Jetpack Navigation Compose

## **Dependencies** 
* Firebase Android SDK (com.google.firebase:firebase-database-ktx)
* Jetpack Compose libraries
* Material3 for UI components

# **Project Structure** 
The project consists of four main Kotlin files:
1. **MainActivity.kt**
* The entry point of the app, setting up Jetpack Compose and navigation.
* Contains composables for:
  * Home: Collects the user’s name and navigates to the Quiz screen
  * Quiz: Fetches questions from Firebase, displays them, tracks answers, and manages review mode.
  * ReviewQuestions: Displays the user’s answers with correct/incorrect highlights.
  * Score: A placeholder screen for displaying the final score (currently minimal).
  * Uses NavHost for navigation between screens

2. **Question.kt**
* Defines the Question data class, which represents a quiz question
 ````
data class Question(
    val question: String = "",
    val choices: Map<String, String> = mapOf(),
    val correctAnswer: String = ""
)
````
* Matches the JSON structure in Firebase Realtime Database.

3. **ChosenAnswers.kt**
* Defines the ChosenAnswers data class to store user responses
````
data class ChosenAnswers(
    val question: Question,
    val ChosenAnswersKey: String,
    val isCorrect: Boolean
)
````
* Used in review mode to track each question’s selected answer and correctness.

4. **UIComponent.kt**

* Defines two reusable Jetpack Compose UI components for the TrivSec quiz app:
  * StandardButton: A customizable button with a consistent blue style (Color(0xFF4285F4)), fixed 48.dp height, and 16.sp text. It supports enabling/disabling and triggers an action on click. Used for buttons like "Start Quiz" or "Next" to ensure a uniform look.
  * QuizCard: A styled card with a white background, 4.dp elevation, and 8.dp outer padding. It wraps content (e.g., questions or answers) with 16.dp inner padding, providing a clean, elevated display. Used to present quiz content consistently across screens.

# **Firebase Integration**
The app uses Firebase Realtime Database to store and retrieve quiz questions. The database structure is as follows:
````
{
  "questions": {
    "question1": {
      "id": "question1",
      "category": "Cybersecurity",
      "questionText": "Which of the following is a common method used in phishing attacks?",
      "options": {
        "A": "Using strong encryption algorithms",
        "B": "Installing a firewall",
        "C": "Sending fraudulent emails to trick users",
        "D": "Regular software updates"
      },
      "correctAnswer": "C",
      "explanation": "Phishing attacks often involve sending fake emails that appear legitimate to trick users into revealing sensitive information."
    }
  }
}

````
## **Firebase Usage**
* Initialization: In Quiz, the app initializes Firebase with Firebase.database and references the "Questions" node using database.getReference("Questions").
* Data Fetching: Uses ref.get() in a LaunchedEffect to perform a one-time fetch of questions when the Quiz screen loads.
* Data Mapping: Converts JSON data to Question objects using getValue(Question::class.java).
* Error Handling: Logs errors via addOnFailureListener (e.g., for network or permission issues).

# **Prerequisites**
To run the app, ensure you have:
* Android Studio (latest stable version)
* Kotlin 1.9.0 or higher
* A Firebase project with Realtime Database enabled
* The google-services.json file from Firebase, placed in the app/ directory
* An Android device or emulator (API 21 or higher)

# **Setup Instructions**
1. Clone the Repository:
`git clone https://github.com/RyanK-04/TRIVSEC.git`

2. **Set Up Firebase**
* Create a Firebase project at Firebase Console.
* Enable Realtime Database.
* Download the google-services.json file and place it in the app/ directory.
* Add Firebase dependencies to build.gradle

````
// Project-level build.gradle
plugins {
    id 'com.google.gms.google-services' version '4.4.2' apply false
}

// App-level build.gradle
dependencies {
    implementation platform('com.google.firebase:firebase-bom:33.1.0')
    implementation 'com.google.firebase:firebase-database-ktx'
    implementation 'androidx.compose.material3:material3'
    implementation 'androidx.navigation:navigation-compose:2.7.7'
}
````
3. Add Questions to the database 
* In the Firebase Realtime Database, create a "Questions" node with the structure shown above.
* Example: Add questions manually via the Firebase Console or import a JSON file.

4. **Sync and Run**
   * Open the project in Android Studio.
   * Sync the project with Gradle.
   * Run the app on an emulator or device.

# **How It Works**
1. Home Screen:
   * Users enter their name in a TextField.
   * Clicking "Start Quiz" navigates to the Quiz screen, passing the username via navigation arguments.

2. Quiz Screen:
   * Fetches questions from Firebase Realtime Database.
   * Displays one question at a time with radio buttons for answer choices.
   * Tracks the user’s selected answer and checks if it matches correctAnswer
   * Stores each response in a ChosenAnswers list and updates the score.
   * After the last question, shows the score and an optional award image for a perfect score.

3. Review Mode:
   * Displays all questions, the user’s answers, and whether they were correct (green) or incorrect (red).
   * Allows users to return to the Home screen.

4. Score Screen:
   * A placeholder screen (currently minimal) that could display the final score or additional stats.
   
# ****Future Improvements****
* Firebase Authentication: Add user login to save scores and track progress.
* Score Storage: Store user scores in Firebase for leaderboards.
* Real-Time Updates: Use addValueEventListener to fetch questions in real-time.
* Enhanced Error Handling: Show user-friendly error messages for failed Firebase requests.
* Image Support: Use Firebase Storage to include images in questions.
* Security Rules: Implement Firebase Security Rules to restrict database access (e.g., read-only for questions).

# **License**
This project is licensed under the MIT License.
````
MIT License

Copyright (c) 2025 C.Dominion

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
````

