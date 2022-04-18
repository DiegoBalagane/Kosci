package com.example.lab2zaddom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        val actionBar = supportActionBar // helpful var
        actionBar!!.title = "Zasady gry" // changing title of activity
        actionBar.setDisplayHomeAsUpEnabled(true) // displaying back button
    }
    // going back after pressing back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}