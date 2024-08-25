package com.example.hopedonationapp.activity

import androidx.navigation.NavController
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hopedonationapp.R

class MainActivity : AppCompatActivity() { // Activity == Screens
    // App Compact is used for backward compatibility + loading of dependencies,,,,,
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        // inflates the layout , creates connection between layout and Main Acitvity
        // R class is for recourse...   R.layout.activity_main -> conneects Main with xml representing layout elements
        //without this everything is 0/100
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}