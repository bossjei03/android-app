package com.example.firebaseauthapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        loadAnnouncements()
        loadEvents()
        loadReminders()
        setupQuickActions()
    }

    private fun loadAnnouncements() {
        val announcementsListView = findViewById<ListView>(R.id.announcementsListView)
        val noAnnouncementsText = findViewById<TextView>(R.id.noAnnouncementsText)

        db.collection("announcements")
            .whereEqualTo("targetAudience", "all")
            .orWhereEqualTo("targetAudience", "students")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { documents ->
                val announcements = mutableListOf<String>()
                for (document in documents) {
                    val title = document.getString("title") ?: "No Title"
                    val content = document.getString("content") ?: ""
                    announcements.add("$title: $content")
                }

                if (announcements.isEmpty()) {
                    noAnnouncementsText.visibility = android.view.View.VISIBLE
                    announcementsListView.visibility = android.view.View.GONE
                } else {
                    noAnnouncementsText.visibility = android.view.View.GONE
                    announcementsListView.visibility = android.view.View.VISIBLE
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, announcements)
                    announcementsListView.adapter = adapter
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load announcements", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadEvents() {
        val eventsListView = findViewById<ListView>(R.id.eventsListView)
        val noEventsText = findViewById<TextView>(R.id.noEventsText)

        // For this example, we'll use announcements as events
        // In a real app, you'd have a separate "events" collection
        db.collection("announcements")
            .whereEqualTo("targetAudience", "all")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener { documents ->
                val events = mutableListOf<String>()
                for (document in documents) {
                    val title = document.getString("title") ?: "Event"
                    events.add("📅 $title")
                }

                if (events.isEmpty()) {
                    noEventsText.visibility = android.view.View.VISIBLE
                    eventsListView.visibility = android.view.View.GONE
                } else {
                    noEventsText.visibility = android.view.View.GONE
                    eventsListView.visibility = android.view.View.VISIBLE
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, events)
                    eventsListView.adapter = adapter
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadReminders() {
        val remindersListView = findViewById<ListView>(R.id.remindersListView)
        val noRemindersText = findViewById<TextView>(R.id.noRemindersText)

        val userId = auth.currentUser?.uid ?: return

        // Load assignments as reminders
        db.collection("assignments")
            .whereGreaterThan("dueDate", com.google.firebase.Timestamp.now())
            .orderBy("dueDate")
            .limit(5)
            .get()
            .addOnSuccessListener { documents ->
                val reminders = mutableListOf<String>()
                for (document in documents) {
                    val title = document.getString("title") ?: "Assignment"
                    val dueDate = document.getTimestamp("dueDate")?.toDate()?.toString() ?: "No due date"
                    reminders.add("⏰ $title - Due: $dueDate")
                }

                if (reminders.isEmpty()) {
                    noRemindersText.visibility = android.view.View.VISIBLE
                    remindersListView.visibility = android.view.View.GONE
                } else {
                    noRemindersText.visibility = android.view.View.GONE
                    remindersListView.visibility = android.view.View.VISIBLE
                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reminders)
                    remindersListView.adapter = adapter
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load reminders", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupQuickActions() {
        val viewGradesButton = findViewById<Button>(R.id.viewGradesButton)
        val viewAttendanceButton = findViewById<Button>(R.id.viewAttendanceButton)

        viewGradesButton.setOnClickListener {
            Toast.makeText(this, "View Grades - Feature coming soon!", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to grades activity
        }

        viewAttendanceButton.setOnClickListener {
            Toast.makeText(this, "View Attendance - Feature coming soon!", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to attendance activity
        }
    }
}
