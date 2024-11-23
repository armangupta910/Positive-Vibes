package com.example.positivevibes

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Retrieve SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        var userId = sharedPreferences.getString("user_id", null)

        // If UID doesn't exist, create a new one and save it in SharedPreferences
        if (userId == null) {
            userId = UUID.randomUUID().toString() // Generate a new unique ID
            val editor = sharedPreferences.edit()
            editor.putString("user_id", userId)
            editor.apply() // Save the UID to SharedPreferences
        }

        // Delay for 3 seconds before transitioning to HomeActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Homescreen::class.java)
            startActivity(intent)
            finish() // Finish SplashActivity so the user can't return to it
        }, 3000) // 3000ms = 3 seconds


    }
}