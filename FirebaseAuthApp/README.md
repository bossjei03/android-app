# FirebaseAuthApp

A simple Android app demonstrating Firebase Authentication with email and password in Kotlin, including role-based redirection.

## Features
- User Sign Up (defaults to "student" role)
- User Sign In
- User Sign Out
- Role-based redirection (Admin, Teacher, Student)
- Basic UI with email and password fields

## Prerequisites
- Android Studio (latest version)
- A Google account for Firebase
- Android device or emulator (API 21+)

## Setup Instructions

### 1. Clone or Download the Project
- Open Android Studio.
- Import the `FirebaseAuthApp` project from the `FirebaseAuthApp` directory.

### 2. Set Up Firebase
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new project or select an existing one.
3. Enable Email/Password authentication:
   - Go to Authentication > Sign-in method.
   - Enable "Email/Password".
4. Enable Firestore:
   - Go to Firestore Database > Create database.
   - Choose "Start in test mode" for development.
5. Download the `google-services.json` file from Project settings > General > Your apps.
6. Replace the placeholder `google-services.json` in the `app/` directory with your downloaded file.

### 3. Assign Roles (Optional)
- To assign roles other than "student", you can manually update the Firestore database:
  - Go to Firestore Database in Firebase Console.
  - Add a document in the "users" collection with the user ID as document ID.
  - Add a field "role" with value "admin", "teacher", or "student".
- For production, implement an admin interface to assign roles.

### 4. Build and Run
1. Open the project in Android Studio.
2. Sync the project with Gradle files (File > Sync Project with Gradle Files).
3. Run the app on an emulator or device.

## Project Structure
- `MainActivity.kt`: Handles authentication logic, role retrieval, and redirection.
- `AdminActivity.kt`, `TeacherActivity.kt`, `StudentActivity.kt`: Role-specific activities.
- `activity_main.xml`, `activity_admin.xml`, etc.: Layouts for each screen.
- `build.gradle`: Includes Firebase Auth and Firestore dependencies.
- `google-services.json`: Firebase configuration (replace with your own).

## Usage
- Enter email and password.
- Tap "Sign Up" to create a new account (assigned "student" role by default).
- Tap "Sign In" to log in and be redirected based on your role.
- Tap "Sign Out" to log out.

## Dependencies
- Firebase Auth: `com.google.firebase:firebase-auth-ktx:22.3.0`
- Firebase Firestore: `com.google.firebase:firebase-firestore-ktx:24.9.1`
- AndroidX libraries for UI and lifecycle.

## Notes
- This is a basic implementation. For production, add proper error handling, UI improvements, and security measures.
- Roles are stored in Firestore under the "users" collection.
- Default role for new sign-ups is "student". Modify the code to change this.
- Refer to [Firebase Docs](https://firebase.google.com/docs/auth/android/start) for more advanced features.
