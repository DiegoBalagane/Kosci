package com.example.lab2zaddom

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.iterator
import com.google.android.material.snackbar.Snackbar

class SettingsActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    lateinit var numberFormat: String
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        numberFormat = intent.getStringExtra(getString(R.string.numberFormatKey)) ?: "5 dices"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        // Choosing given option
        when(numberFormat){
            "5 dices" -> findViewById<RadioButton>(R.id.firstOption).isChecked = true
            "3 dices" -> findViewById<RadioButton>(R.id.secondOption).isChecked = true
            else -> findViewById<RadioButton>(R.id.firstOption).isChecked = true
        }

        val actionBar = supportActionBar // helpful var
        actionBar!!.title = "Ustawienia" // changing title of activity
        actionBar.setDisplayHomeAsUpEnabled(true) // displaying back button
        // Function which makes that player can't click on apply button if value isnt changed
        findViewById<RadioGroup>(R.id.dicesGroup).setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if(radio == findViewById<RadioButton>(R.id.firstOption)) {
                findViewById<Button>(R.id.applyButton).isEnabled = numberFormat != "5 dices"
            } else {
                findViewById<Button>(R.id.applyButton).isEnabled = numberFormat != "3 dices"
            }
        }
        // Reseting game
        val resetBtn = findViewById<Button>(R.id.resetButton)
        resetBtn.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Uwaga")
            alert.setMessage("Gra zostanie zresetowana i poprzedni wynik zostanie utracony. Czy na pewno chcesz kontynuować?")
            alert.setPositiveButton("RESETUJ GRĘ"){dialog, which-> resetChanges() }
            alert.setNegativeButton("ANULUJ"){dialog, which->dialog.cancel()}
            alert.show()
        }
        // Apply changes
        val applyButton: Button = findViewById(R.id.applyButton)
        applyButton.setOnClickListener {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Uwaga")
            alert.setMessage("Zmiana ilości kości do rzucania spowoduje zresetowanie gry. Czy na pewno chcesz kontynuować?")
            alert.setPositiveButton("ZAPISZ ZMIANY"){dialog, which-> setChanges() }
            alert.setNegativeButton("ANULUJ"){dialog, which->dialog.cancel()}
            alert.show()
        }
    }

    private fun setChanges() {
        applyChanges()
        finish()
    }

    private fun resetChanges() {
        applyReset()
        finish()
    }

    private fun applyChanges() {
        val returnIntent = Intent()
        if(findViewById<RadioButton>(R.id.firstOption).isChecked) {
            returnIntent.putExtra(getString(R.string.numberFormatKey), "5 dices")
        } else {
            returnIntent.putExtra(getString(R.string.numberFormatKey), "3 dices")
        }
        setResult(RESULT_OK,returnIntent)
    }

    private fun applyReset() {
        val returnIntent = Intent()
        returnIntent.putExtra(getString(R.string.numberFormatKey), "reset")
        setResult(RESULT_OK,returnIntent)
    }

    // going back after pressing back button
     override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}