# Creating an Android Studio Project in Kotlin with Firebase Authentication

This guide provides step-by-step instructions to create a new Android Studio project using Kotlin and integrate Firebase Authentication.

## Prerequisites
- Android Studio (latest version recommended)
- A Google account for Firebase
- Basic knowledge of Kotlin and Android development

## Step 1: Create a New Android Studio Project
1. Open Android Studio.
2. Click on "Start a new Android Studio project" or "File" > "New" > "New Project".
3. Select "Empty Activity" template.
4. Configure your project:
   - Name: Enter your app name (e.g., "FirebaseAuthApp")
   - Package name: Use a unique package name (e.g., "com.example.firebaseauthapp")
   - Save location: Choose a directory (you can use the current directory or a subdirectory)
   - Language: Select "Kotlin"
   - Minimum SDK: API 21 (Android 5.0) or higher
5. Click "Finish" to create the project.

## Step 2: Set Up Firebase
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Click "Create a project" or select an existing project.
3. Follow the setup wizard to create your Firebase project.
4. In your Firebase project:
   - Go to "Authentication" in the left sidebar.
   - Click "Get started".
   - Go to the "Sign-in method" tab.
   - Enable the authentication providers you want (e.g., Email/Password, Google, etc.).
   - Save your changes.

## Step 3: Add Firebase to Your Android Project
1. In Android Studio, click on "Tools" > "Firebase".
2. In the Firebase Assistant window, select "Authentication" > "Authenticate using Firebase".
3. Click "Connect to Firebase".
4. Select your Firebase project and click "Connect".
5. Click "Add Firebase Authentication to your app".
6. Accept the changes to add the necessary dependencies and configurations.

Alternatively, manually add Firebase:
1. In your project-level `build.gradle` file, add the Google services plugin:
   ```gradle
   buildscript {
       dependencies {
           classpath 'com.google.gms:google-services:4.3.15'
       }
   }
   ```
2. In your app-level `build.gradle` file, add Firebase dependencies:
   ```gradle
   plugins {
       id 'com.android.application'
       id 'kotlin-android'
       id 'com.google.gms.google-services'
   }

   dependencies {
       implementation 'com.google.firebase:firebase-auth-ktx:22.1.0'
       // Add other Firebase dependencies as needed
   }
   ```
3. Download the `google-services.json` file from your Firebase project settings and place it in the `app/` directory.

## Step 4: Implement Firebase Authentication
1. In your `MainActivity.kt`, initialize Firebase Auth:
   ```kotlin
   import com.google.firebase.auth.FirebaseAuth

   class MainActivity : AppCompatActivity() {
       private lateinit var auth: FirebaseAuth

       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           setContentView(R.layout.activity_main)

           auth = FirebaseAuth.getInstance()
       }
   }
   ```

2. Create sign-up functionality:
   ```kotlin
   private fun signUp(email: String, password: String) {
       auth.createUserWithEmailAndPassword(email, password)
           .addOnCompleteListener(this) { task ->
               if (task.isSuccessful) {
                   // Sign up success
                   val user = auth.currentUser
                   // Update UI
               } else {
                   // Sign up failed
                   // Handle error
               }
           }
   }
   ```

3. Create sign-in functionality:
   ```kotlin
   private fun signIn(email: String, password: String) {
       auth.signInWithEmailAndPassword(email, password)
           .addOnCompleteListener(this) { task ->
               if (task.isSuccessful) {
                   // Sign in success
                   val user = auth.currentUser
                   // Update UI
               } else {
                   // Sign in failed
                   // Handle error
               }
           }
   }
   ```

4. Check current user and sign out:
   ```kotlin
   private fun checkCurrentUser() {
       val user = auth.currentUser
       if (user != null) {
           // User is signed in
       } else {
           // No user is signed in
       }
   }

   private fun signOut() {
       auth.signOut()
       // Update UI
   }
   ```

## Step 5: Update UI
1. Modify your `activity_main.xml` to include login/signup fields:
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       android:orientation="vertical"
       android:padding="16dp">

       <EditText
           android:id="@+id/emailEditText"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           android:hint="Email"
           android:inputType="textEmailAddress" />

       <EditText
           android:id="@+id/passwordEditText"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           android:hint="Password"
           android:inputType="textPassword" />

       <Button
           android:id="@+id/signInButton"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           android:text="Sign In" />

       <Button
           android:id="@+id/signUpButton"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           android:text="Sign Up" />

       <Button
           android:id="@+id/signOutButton"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           android:text="Sign Out" />

   </LinearLayout>
   ```

2. Connect the UI elements in your activity:
   ```kotlin
   private lateinit var emailEditText: EditText
   private lateinit var passwordEditText: EditText
   private lateinit var signInButton: Button
   private lateinit var signUpButton: Button
   private lateinit var signOutButton: Button

   override fun onCreate(savedInstanceState: Bundle?) {
       // ... existing code ...

       emailEditText = findViewById(R.id.emailEditText)
       passwordEditText = findViewById(R.id.passwordEditText)
       signInButton = findViewById(R.id.signInButton)
       signUpButton = findViewById(R.id.signUpButton)
       signOutButton = findViewById(R.id.signOutButton)

       signInButton.setOnClickListener {
           val email = emailEditText.text.toString()
           val password = passwordEditText.text.toString()
           signIn(email, password)
       }

       signUpButton.setOnClickListener {
           val email = emailEditText.text.toString()
           val password = passwordEditText.text.toString()
           signUp(email, password)
       }

       signOutButton.setOnClickListener {
           signOut()
       }
   }
   ```

## Step 6: Test Your App
1. Run your app on an emulator or physical device.
2. Test sign-up, sign-in, and sign-out functionality.
3. Check the Firebase Console to see authenticated users.

## Additional Tips
- Handle authentication state changes by implementing `AuthStateListener`.
- Add proper error handling and user feedback.
- Consider using FirebaseUI for a more polished authentication experience.
- Always secure your Firebase project rules in production.

For more detailed information, refer to the [Firebase Authentication documentation](https://firebase.google.com/docs/auth/android/start).
