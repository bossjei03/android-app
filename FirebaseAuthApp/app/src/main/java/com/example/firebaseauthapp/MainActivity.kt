package com.example.firebaseauthapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val signInButton = findViewById<Button>(R.id.signInButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val signOutButton = findViewById<Button>(R.id.signOutButton)

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

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        getUserRole(user.uid)
                    }
                } else {
                    Toast.makeText(this, "Sign in failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Default role is student, you can modify this logic
                        saveUserRole(user.uid, "student")
                        redirectToActivity("student")
                    }
                } else {
                    Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signOut() {
        auth.signOut()
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
    }

    private fun getUserRole(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val role = document.getString("role") ?: "student"
                redirectToActivity(role)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to get user role", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserRole(userId: String, role: String) {
        val user = hashMapOf("role" to role)
        db.collection("users").document(userId).set(user)
    }

    private fun redirectToActivity(role: String) {
        val intent = when (role) {
            "admin" -> Intent(this, AdminActivity::class.java)
            "teacher" -> Intent(this, TeacherActivity::class.java)
            "student" -> Intent(this, StudentActivity::class.java)
            else -> Intent(this, StudentActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
