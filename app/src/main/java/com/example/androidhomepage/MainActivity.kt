package com.example.androidhomepage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        // Only authenticated users may view the existing home page.
        if (auth.currentUser == null) {
            openLogin()
            return
        }

        setContentView(R.layout.activity_main)

        val superButton = findViewById<Button>(R.id.superButton)
        superButton.setOnClickListener { showAccountMenu() }

        findViewById<Button>(R.id.notificationButton).setOnClickListener {
            Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.viewAllButton).setOnClickListener {
            Toast.makeText(this, "Showing all activities", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        if (::auth.isInitialized && auth.currentUser == null) openLogin()
    }

    private fun showAccountMenu() {
        val email = auth.currentUser?.email ?: "Signed-in user"
        AlertDialog.Builder(this)
            .setTitle("Account")
            .setMessage("Signed in as:\n$email\n\nWould you like to log out?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Log out") { _, _ ->
                auth.signOut()
                Toast.makeText(this, "You have logged out.", Toast.LENGTH_SHORT).show()
                openLogin()
            }
            .show()
    }

    private fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
