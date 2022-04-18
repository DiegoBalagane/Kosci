package com.example.lab2zaddom

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/*
Code is very long and it looks bad, because its my 1st Kotlin app and I was learning during creating that app,
so I didnt want to change everything
 */

class MainActivity() : AppCompatActivity() {
    private var currentFormat: String = "5 dices"
    private var format: String = currentFormat
    var amountOfLeftThrows: Int = 3
    var nextThrow: Int = 3
    var currentThrow: Int = 3
    var numberOfThrow: Int = 1
    private val dices = arrayOf(0, 0, 0, 0, 0)
    var wasThrown: Boolean = false
    var firstBonusAdded: Boolean = false
    var secondBonusAdded: Boolean = false
    var firstPlayerPoints: Int = 0
    var secondPlayerPoints: Int = 0
    var firstPlayerBonus: Int = 0
    var secondPlayerBonus: Int = 0
    //Starting settings activity
    @RequiresApi(Build.VERSION_CODES.M)
    private val launchSettingsActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK){
            format = result.data?.getStringExtra(getString(R.string.numberFormatKey)) ?: "5 dices"
            if(format == "5 dices") {
                currentFormat = format
                resetValues()
                Snackbar.make(findViewById(R.id.mainContainer), "Wybrano tryb gry dla 5 kości. Gra została zresetowana", Snackbar.LENGTH_SHORT).show()

            } else if(format == "3 dices") {
                currentFormat = format
                resetValues()
                Snackbar.make(findViewById(R.id.mainContainer), "Wybrano tryb gry dla 3 kości. Gra została zresetowana", Snackbar.LENGTH_SHORT).show()
            } else {
                resetValues()
                Snackbar.make(findViewById(R.id.mainContainer), "Gra została zresetowana", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startSettingsActivity() {
        val intent = Intent(this,SettingsActivity::class.java)

        intent.putExtra(getString(R.string.numberFormatKey),currentFormat)

        launchSettingsActivity.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resetValues()
        //Creating spinner to choose amount of throws
        val spinner: Spinner = findViewById(R.id.throwChoice)
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (position) {
                    0 -> {
                        nextThrow = 3
                    }
                    1 -> {
                        nextThrow = 2
                    }
                    2 -> {
                        nextThrow = 1
                    }
                    else -> {
                        nextThrow = 3
                    }
                }
                if(!wasThrown){
                    currentThrow = nextThrow
                    amountOfLeftThrows = nextThrow
                    changingThrowsText()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        // clicking on button to roll dices
        fab.setOnClickListener {
            //Player who throw's text is bold
            if(numberOfThrow % 2 == 1) {
                findViewById<TextView>(R.id.firstPlayerFirstTableTxt).setTypeface(null, Typeface.BOLD)
                findViewById<TextView>(R.id.firstPlayerSecondTableTxt).setTypeface(null, Typeface.BOLD)
                findViewById<TextView>(R.id.secondPlayerFirstTableTxt).setTypeface(null, Typeface.NORMAL)
                findViewById<TextView>(R.id.secondPlayerSecondTableTxt).setTypeface(null, Typeface.NORMAL)
            } else {
                findViewById<TextView>(R.id.firstPlayerFirstTableTxt).setTypeface(null, Typeface.NORMAL)
                findViewById<TextView>(R.id.firstPlayerSecondTableTxt).setTypeface(null, Typeface.NORMAL)
                findViewById<TextView>(R.id.secondPlayerFirstTableTxt).setTypeface(null, Typeface.BOLD)
                findViewById<TextView>(R.id.secondPlayerSecondTableTxt).setTypeface(null, Typeface.BOLD)
            }
            // Snackbar taht you cant throw more times
            if(amountOfLeftThrows == 0) {
                val snack = Snackbar.make(findViewById(R.id.mainContainer), "Nie możesz już rzucać! Wybierz jedną z dostępnych opcji",Snackbar.LENGTH_LONG)
                snack.show()
            }
            //Rolling dices
            if(amountOfLeftThrows> 0) {
                wasThrown = true
                amountOfLeftThrows--
                rollDice() // rolling
                setResults(numberOfThrow)
            }
            changingThrowsText()
        }

        val actionBar = supportActionBar // helpful var
        actionBar!!.title = "Kości" // changing title of activity
    }
    // Function which shows possible points to reach after rolling dices
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setResults(number: Int) {
        val first = findViewById<Button>(R.id.firstOptionFirstPlayerButton)
        val second = findViewById<Button>(R.id.secondOptionFirstPlayerButton)
        val third = findViewById<Button>(R.id.thirdOptionFirstPlayerButton)
        val fourth = findViewById<Button>(R.id.fourthOptionFirstPlayerButton)
        val fifth = findViewById<Button>(R.id.fifthOptionFirstPlayerButton)
        val sixth = findViewById<Button>(R.id.sixthOptionFirstPlayerButton)
        val seventh = findViewById<Button>(R.id.seventhOptionFirstPlayerButton)
        val eighth = findViewById<Button>(R.id.eighthOptionFirstPlayerButton)
        val ninth = findViewById<Button>(R.id.ninthOptionFirstPlayerButton)
        val tenth = findViewById<Button>(R.id.tenthOptionFirstPlayerButton)
        val eleventh = findViewById<Button>(R.id.eleventhOptionFirstPlayerButton)
        val twelfth = findViewById<Button>(R.id.twelfthOptionFirstPlayerButton)
        val thirteenth = findViewById<Button>(R.id.thirteenthOptionFirstPlayerButton)
        val sfirst = findViewById<Button>(R.id.firstOptionSecondPlayerButton)
        val ssecond = findViewById<Button>(R.id.secondOptionSecondPlayerButton)
        val sthird = findViewById<Button>(R.id.thirdOptionSecondPlayerButton)
        val sfourth = findViewById<Button>(R.id.fourthOptionSecondPlayerButton)
        val sfifth = findViewById<Button>(R.id.fifthOptionSecondPlayerButton)
        val ssixth = findViewById<Button>(R.id.sixthOptionSecondPlayerButton)
        val sseventh = findViewById<Button>(R.id.seventhOptionSecondPlayerButton)
        val seighth = findViewById<Button>(R.id.eighthOptionSecondPlayerButton)
        val sninth = findViewById<Button>(R.id.ninthOptionSecondPlayerButton)
        val stenth = findViewById<Button>(R.id.tenthOptionSecondPlayerButton)
        val seleventh = findViewById<Button>(R.id.eleventhOptionSecondPlayerButton)
        val stwelfth = findViewById<Button>(R.id.twelfthOptionSecondPlayerButton)
        val sthirteenth = findViewById<Button>(R.id.thirteenthOptionSecondPlayerButton)
        if(number % 2 == 1) {
            sfirst.isClickable = false
            ssecond.isClickable = false
            sthird.isClickable = false
            sfourth.isClickable = false
            sfifth.isClickable = false
            ssixth.isClickable = false
            sseventh.isClickable = false
            seighth.isClickable = false
            sninth.isClickable = false
            stenth.isClickable = false
            seleventh.isClickable = false
            stwelfth.isClickable = false
            sthirteenth.isClickable = false
            //That if makes that we show points only at buttons which werent chosen
            if(first.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                first.text = getJeden()
                blockingOptionButton(first)
            }
            if(second.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                second.text = getDwa()
                blockingOptionButton(second)
            }
            if(third.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                third.text = getTrzy()
                blockingOptionButton(third)
            }
            if(fourth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                fourth.text = getCztery()
                blockingOptionButton(fourth)
            }
            if(fifth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                fifth.text = getPiec()
                blockingOptionButton(fifth)
            }
            if(sixth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                sixth.text = getSzesc()
                blockingOptionButton(sixth)
            }
            if(seventh.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                seventh.text = getTrojka()
                blockingOptionButton(seventh)
            }
            if(eighth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                eighth.text = getKareta()
                blockingOptionButton(eighth)
            }
            if(ninth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                ninth.text = getFul()
                blockingOptionButton(ninth)
            }
            if(tenth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                tenth.text = getMalyStrit()
                blockingOptionButton(tenth)
            }
            if(eleventh.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                eleventh.text = getDuzyStrit()
                blockingOptionButton(eleventh)
            }
            if(twelfth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                twelfth.text = getGeneral()
                blockingOptionButton(twelfth)
            }
            if(thirteenth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                thirteenth.text = getSzansa()
                blockingOptionButton(thirteenth)
            }
        } else {
            first.isClickable = false
            second.isClickable = false
            third.isClickable = false
            fourth.isClickable = false
            fifth.isClickable = false
            sixth.isClickable = false
            seventh.isClickable = false
            eighth.isClickable = false
            ninth.isClickable = false
            tenth.isClickable = false
            eleventh.isClickable = false
            twelfth.isClickable = false
            thirteenth.isClickable = false
            if(sfirst.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                sfirst.text = getJeden()
                blockingOptionButton(sfirst)
            }
            if(ssecond.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                ssecond.text = getDwa()
                blockingOptionButton(ssecond)
            }
            if(sthird.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                sthird.text = getTrzy()
                blockingOptionButton(sthird)
            }
            if(sfourth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                sfourth.text = getCztery()
                blockingOptionButton(sfourth)
            }
            if(sfifth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                sfifth.text = getPiec()
                blockingOptionButton(sfifth)
            }
            if(ssixth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                ssixth.text = getSzesc()
                blockingOptionButton(ssixth)
            }
            if(sseventh.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                sseventh.text = getTrojka()
                blockingOptionButton(sseventh)
            }
            if(seighth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                seighth.text = getKareta()
                blockingOptionButton(seighth)
            }
            if(sninth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                sninth.text = getFul()
                blockingOptionButton(sninth)
            }
            if(stenth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                stenth.text = getMalyStrit()
                blockingOptionButton(stenth)
            }
            if(seleventh.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                seleventh.text = getDuzyStrit()
                blockingOptionButton(seleventh)
            }
            if(stwelfth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                stwelfth.text = getGeneral()
                blockingOptionButton(stwelfth)
            }
            if(sthirteenth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
                sthirteenth.text = getSzansa()
                blockingOptionButton(sthirteenth)
            }
        }

    }
    // Those functions count possible points to reach for given button
    private fun getSzansa(): String {
        var helpful = 0
        for(i in dices.indices) {
                helpful += dices[i]
        }
        return helpful.toString()
    }

    private fun getGeneral(): String {
        for(i in 1..4) {
            if(dices[0] != dices[i]) {
                return "0"
                break
            }
        }
        return "50"
    }

    private fun getDuzyStrit(): String {
        var helpful: Int
        var counter = 0
        for(i in 2..5) {
            helpful = i
            if(counter != (helpful - 2)) {
                return "0"
                break
            }
            for(j in 0..4) {
                if(dices[j] == helpful) {
                    ++counter
                    break
                }
            }
        }
        if(counter == 4) {
            for(i in 0..4) {
                if(dices[i] == 1 || dices[i] == 6) {
                    return "40"
                    break
                }
            }
        }
        return "0"
    }

    private fun getMalyStrit(): String {
        var helpful: Int
        var counter = 0
        for(i in 3..4) {
            helpful = i
            if(counter != (helpful - 3)) {
                return "0"
                break
            }
            for(j in 0..4) {
                if(dices[j] == helpful) {
                    ++counter
                    break
                }
            }
        }
        if(counter == 2) {
            for(i in 0..4) {
                if(dices[i] == 1) {
                    for(j in (i+1)..4) {
                        if(dices[j] == 2) {
                            return "30"
                            break
                        }
                    }
                } else if(dices[i] == 2) {
                    for(j in (i+1)..4) {
                        if(dices[j] == 1 || dices[j] == 5) {
                            return "30"
                            break
                        }
                    }
                } else if(dices[i] == 5) {
                    for(j in (i+1)..4) {
                        if(dices[j] == 2 || dices[j] == 6) {
                            return "30"
                            break
                        }
                    }
                } else if(dices[i] == 6) {
                    for(j in (i+1)..4) {
                        if(dices[j] == 6) {
                            return "30"
                            break
                        }
                    }
                }
            }
        }
        return "0"
    }


    private fun getKareta(): String {
        var counter: Int
        for(i in 0..1) {
            counter = 1
            for(j in (i+1)..4) {
                if(dices[j] == dices[i]) {
                    ++counter
                }
            }
            if(counter == 4) {
                var helpful = 0
                for(j in 0..4) {
                    helpful += dices[j]
                }
                return helpful.toString()
                break
            }
        }
        return "0"
    }

    private fun getTrojka(): String {
        var counter: Int
        for(i in 0..2) {
            counter = 1
            for(j in (i+1)..4) {
                if(dices[j] == dices[i]) {
                    ++counter
                }
            }
            if(counter == 3) {
                var helpful = 0
                for(j in 0..4) {
                    helpful += dices[j]
                }
                return helpful.toString()
                break
            }
        }
        return "0"
    }

    private fun getFul(): String {
        if(getTrojka() != "0") {
            var counter = 1
            for(i in 1..4){
                if(dices[0] == dices[i]){
                    ++counter
                }
            }
            if(counter == 3) {
                for(i in 1..4){
                    if(dices[0] != dices[i]){
                        for(j in (i+1)..4){
                            if(dices[j] == dices[i]) {
                                return "25"
                                break
                            }
                        }
                        break
                    }
                }
            } else if(counter == 2) {
                return "25"
            }
        }
        return "0"
    }

    private fun getTrzy(): String {
        var helpful = 0
        for(i in dices.indices) {
            if(dices[i] == 3) {
                helpful += 3
            }
        }
        return helpful.toString()
    }

    private fun getCztery(): String {
        var helpful = 0
        for(i in dices.indices) {
            if(dices[i] == 4) {
                helpful += 4
            }
        }
        return helpful.toString()
    }

    private fun getPiec(): String {
        var helpful = 0
        for(i in dices.indices) {
            if(dices[i] == 5) {
                helpful += 5
            }
        }
        return helpful.toString()
    }

    private fun getSzesc(): String {
        var helpful = 0
        for(i in dices.indices) {
            if(dices[i] == 6) {
                helpful += 6
            }
        }
        return helpful.toString()
    }

    private fun getDwa(): String {
        var helpful = 0
        for(i in dices.indices) {
            if(dices[i] == 2) {
                helpful += 2
            }
        }
        return helpful.toString()
    }

    private fun getJeden(): String {
        var helpful = 0
        for(i in dices.indices) {
            if(dices[i] == 1) {
                helpful += 1
            }
        }
        return helpful.toString()
    }
    //What happens when we click on button with points
    @RequiresApi(Build.VERSION_CODES.M)
    private fun blockingOptionButton(button: Button) {
        button.setOnClickListener {
            if(amountOfLeftThrows != currentThrow) {
                button.isClickable = false
                button.backgroundTintList = getColorStateList(android.R.color.holo_blue_light)
                clearingDices()
                amountOfLeftThrows = nextThrow
                currentThrow = nextThrow
                changingThrowsText()
                wasThrown = false
                var  beka: String = button.text as String
                if(beka != "") {

                if(numberOfThrow % 2 == 1) {
                    firstPlayerPoints += beka.toInt()
                    var resultFirstPlayer = findViewById<TextView>(R.id.pointsFirstPlayerTxt)
                    resultFirstPlayer.text = firstPlayerPoints.toString()
                    if((button == findViewById<Button>(R.id.firstOptionFirstPlayerButton)) or
                        (button == findViewById<Button>(R.id.secondOptionFirstPlayerButton)) or
                        (button == findViewById<Button>(R.id.thirdOptionFirstPlayerButton)) or
                        (button == findViewById<Button>(R.id.fourthOptionFirstPlayerButton)) or
                        (button == findViewById<Button>(R.id.fifthOptionFirstPlayerButton)) or
                        (button == findViewById<Button>(R.id.sixthOptionFirstPlayerButton))){
                        firstPlayerBonus += beka.toInt()
                        findViewById<TextView>(R.id.bonusFirstPlayer).text = firstPlayerBonus.toString()+"/63"
                    }
                    if((firstPlayerBonus >= 63) and !firstBonusAdded){
                        firstBonusAdded = true
                        firstPlayerPoints += 35
                        findViewById<TextView>(R.id.pointsFirstPlayerTxt).text = firstPlayerPoints.toString()
                        val snack = Snackbar.make(findViewById(R.id.mainContainer), "Bonus 35 punktów został przydzielony Graczowi 1",Snackbar.LENGTH_LONG)
                        snack.show()
                    }
                } else {
                    secondPlayerPoints += beka.toInt()
                    var resultSecondPlayer = findViewById<TextView>(R.id.pointsSecondPlayerTxt)
                    resultSecondPlayer.text = secondPlayerPoints.toString()
                    if((button == findViewById<Button>(R.id.firstOptionSecondPlayerButton)) or
                        (button == findViewById<Button>(R.id.secondOptionSecondPlayerButton)) or
                        (button == findViewById<Button>(R.id.thirdOptionSecondPlayerButton)) or
                        (button == findViewById<Button>(R.id.fourthOptionSecondPlayerButton)) or
                        (button == findViewById<Button>(R.id.fifthOptionSecondPlayerButton)) or
                        (button == findViewById<Button>(R.id.sixthOptionSecondPlayerButton))){
                        secondPlayerBonus += beka.toInt()
                        findViewById<TextView>(R.id.bonusSecondPlayer).text = secondPlayerBonus.toString()+"/63"
                    }
                    if((secondPlayerBonus >= 63) and !secondBonusAdded){
                        secondBonusAdded = true
                        secondPlayerPoints += 35
                        findViewById<TextView>(R.id.pointsSecondPlayerTxt).text = secondPlayerPoints.toString()
                        val snack = Snackbar.make(findViewById(R.id.mainContainer), "Bonus 35 punktów został przydzielony Graczowi 2",Snackbar.LENGTH_LONG)
                        snack.show()
                    }
                }
                    findViewById<TextView>(R.id.firstPlayerFirstTableTxt).setTypeface(null, Typeface.NORMAL)
                    findViewById<TextView>(R.id.firstPlayerSecondTableTxt).setTypeface(null, Typeface.NORMAL)
                    findViewById<TextView>(R.id.secondPlayerFirstTableTxt).setTypeface(null, Typeface.NORMAL)
                    findViewById<TextView>(R.id.secondPlayerSecondTableTxt).setTypeface(null, Typeface.NORMAL)
                ++numberOfThrow
                    // end of game
                if(numberOfThrow == 27) {
                    settingEndingAlert()
                    resetValues()
                }
                clearingOtherButtons()
            } }
        }
    }
    // Showing alert when game is finished
    @RequiresApi(Build.VERSION_CODES.M)
    private fun settingEndingAlert() {
        val alert = AlertDialog.Builder(this)
        if(firstPlayerPoints > secondPlayerPoints) {
            alert.setTitle("Gracz 1 wygrał!")
        } else if(firstPlayerPoints < secondPlayerPoints) {
            alert.setTitle("Gracz 2 wygrał!")
        } else {
            alert.setTitle("Pojedynek zakończył się remisem")
        }
        alert.setMessage("Wynik to "+firstPlayerPoints+":"+secondPlayerPoints)
        alert.setPositiveButton("ZACZNIJ NOWĄ GRĘ"){dialog, which->resetValues()}
        alert.setNeutralButton("USTAWIENIA"){dialog, which->startSettingsActivity()}
        alert.show()
    }
    // Helpful function which clears buttons
    @RequiresApi(Build.VERSION_CODES.M)
    private fun clearingOtherButtons() {
        val first = findViewById<Button>(R.id.firstOptionFirstPlayerButton)
        val second = findViewById<Button>(R.id.secondOptionFirstPlayerButton)
        val third = findViewById<Button>(R.id.thirdOptionFirstPlayerButton)
        val fourth = findViewById<Button>(R.id.fourthOptionFirstPlayerButton)
        val fifth = findViewById<Button>(R.id.fifthOptionFirstPlayerButton)
        val sixth = findViewById<Button>(R.id.sixthOptionFirstPlayerButton)
        val seventh = findViewById<Button>(R.id.seventhOptionFirstPlayerButton)
        val eighth = findViewById<Button>(R.id.eighthOptionFirstPlayerButton)
        val ninth = findViewById<Button>(R.id.ninthOptionFirstPlayerButton)
        val tenth = findViewById<Button>(R.id.tenthOptionFirstPlayerButton)
        val eleventh = findViewById<Button>(R.id.eleventhOptionFirstPlayerButton)
        val twelfth = findViewById<Button>(R.id.twelfthOptionFirstPlayerButton)
        val thirteenth = findViewById<Button>(R.id.thirteenthOptionFirstPlayerButton)
        val sfirst = findViewById<Button>(R.id.firstOptionSecondPlayerButton)
        val ssecond = findViewById<Button>(R.id.secondOptionSecondPlayerButton)
        val sthird = findViewById<Button>(R.id.thirdOptionSecondPlayerButton)
        val sfourth = findViewById<Button>(R.id.fourthOptionSecondPlayerButton)
        val sfifth = findViewById<Button>(R.id.fifthOptionSecondPlayerButton)
        val ssixth = findViewById<Button>(R.id.sixthOptionSecondPlayerButton)
        val sseventh = findViewById<Button>(R.id.seventhOptionSecondPlayerButton)
        val seighth = findViewById<Button>(R.id.eighthOptionSecondPlayerButton)
        val sninth = findViewById<Button>(R.id.ninthOptionSecondPlayerButton)
        val stenth = findViewById<Button>(R.id.tenthOptionSecondPlayerButton)
        val seleventh = findViewById<Button>(R.id.eleventhOptionSecondPlayerButton)
        val stwelfth = findViewById<Button>(R.id.twelfthOptionSecondPlayerButton)
        val sthirteenth = findViewById<Button>(R.id.thirteenthOptionSecondPlayerButton)
        if(first.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            first.text = ""
        }
        if(second.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            second.text = ""
        }
        if(third.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            third.text = ""
        }
        if(fourth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            fourth.text = ""
        }
        if(fifth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            fifth.text = ""
        }
        if(sixth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            sixth.text = ""
        }
        if(seventh.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            seventh.text = ""
        }
        if(eighth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            eighth.text = ""
        }
        if(ninth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            ninth.text = ""
        }
        if(tenth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            tenth.text = ""
        }
        if(eleventh.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            eleventh.text = ""
        }
        if(twelfth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            twelfth.text = ""
        }
        if(thirteenth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            thirteenth.text = ""
        }
        if(sfirst.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            sfirst.text = ""
        }
        if(ssecond.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            ssecond.text = ""
        }
        if(sthird.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            sthird.text = ""
        }
        if(sfourth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            sfourth.text = ""
        }
        if(sfifth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            sfifth.text = ""
        }
        if(ssixth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            ssixth.text = ""
        }
        if(sseventh.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            sseventh.text = ""
        }
        if(seighth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            seighth.text = ""
        }
        if(sninth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            sninth.text = ""
        }
        if(stenth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            stenth.text = ""
        }
        if(seleventh.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            seleventh.text = ""
        }
        if(stwelfth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            stwelfth.text = ""
        }
        if(sthirteenth.backgroundTintList != getColorStateList(android.R.color.holo_blue_light)) {
            sthirteenth.text = ""
        }
    }
    // Reseting values when game is finished
    @RequiresApi(Build.VERSION_CODES.M)
    fun resetValues() {
        numberOfThrow = 1
        wasThrown = false
        amountOfLeftThrows = nextThrow
        changingThrowsText()
        firstPlayerPoints = 0
        firstPlayerBonus = 0
        var resultFirstPlayer = findViewById<TextView>(R.id.pointsFirstPlayerTxt)
        resultFirstPlayer.text = firstPlayerPoints.toString()
        findViewById<TextView>(R.id.bonusFirstPlayer).text = "0/63"
        secondPlayerPoints = 0
        secondPlayerBonus = 0
        var resultSecondPlayer = findViewById<TextView>(R.id.pointsSecondPlayerTxt)
        resultSecondPlayer.text = secondPlayerPoints.toString()
        findViewById<TextView>(R.id.bonusSecondPlayer).text = "0/63"
        firstBonusAdded = false
        secondBonusAdded = false
        findViewById<TextView>(R.id.firstPlayerFirstTableTxt).setTypeface(null, Typeface.NORMAL)
        findViewById<TextView>(R.id.firstPlayerSecondTableTxt).setTypeface(null, Typeface.NORMAL)
        findViewById<TextView>(R.id.secondPlayerFirstTableTxt).setTypeface(null, Typeface.NORMAL)
        findViewById<TextView>(R.id.secondPlayerSecondTableTxt).setTypeface(null, Typeface.NORMAL)
        val first = findViewById<Button>(R.id.firstOptionFirstPlayerButton)
        val second = findViewById<Button>(R.id.secondOptionFirstPlayerButton)
        val third = findViewById<Button>(R.id.thirdOptionFirstPlayerButton)
        val fourth = findViewById<Button>(R.id.fourthOptionFirstPlayerButton)
        val fifth = findViewById<Button>(R.id.fifthOptionFirstPlayerButton)
        val sixth = findViewById<Button>(R.id.sixthOptionFirstPlayerButton)
        val seventh = findViewById<Button>(R.id.seventhOptionFirstPlayerButton)
        val eighth = findViewById<Button>(R.id.eighthOptionFirstPlayerButton)
        val ninth = findViewById<Button>(R.id.ninthOptionFirstPlayerButton)
        val tenth = findViewById<Button>(R.id.tenthOptionFirstPlayerButton)
        val eleventh = findViewById<Button>(R.id.eleventhOptionFirstPlayerButton)
        val twelfth = findViewById<Button>(R.id.twelfthOptionFirstPlayerButton)
        val thirteenth = findViewById<Button>(R.id.thirteenthOptionFirstPlayerButton)
        val sfirst = findViewById<Button>(R.id.firstOptionSecondPlayerButton)
        val ssecond = findViewById<Button>(R.id.secondOptionSecondPlayerButton)
        val sthird = findViewById<Button>(R.id.thirdOptionSecondPlayerButton)
        val sfourth = findViewById<Button>(R.id.fourthOptionSecondPlayerButton)
        val sfifth = findViewById<Button>(R.id.fifthOptionSecondPlayerButton)
        val ssixth = findViewById<Button>(R.id.sixthOptionSecondPlayerButton)
        val sseventh = findViewById<Button>(R.id.seventhOptionSecondPlayerButton)
        val seighth = findViewById<Button>(R.id.eighthOptionSecondPlayerButton)
        val sninth = findViewById<Button>(R.id.ninthOptionSecondPlayerButton)
        val stenth = findViewById<Button>(R.id.tenthOptionSecondPlayerButton)
        val seleventh = findViewById<Button>(R.id.eleventhOptionSecondPlayerButton)
        val stwelfth = findViewById<Button>(R.id.twelfthOptionSecondPlayerButton)
        val sthirteenth = findViewById<Button>(R.id.thirteenthOptionSecondPlayerButton)
        first.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        second.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        third.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        fourth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        fifth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        sixth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        seventh.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        eighth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        ninth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        tenth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        eleventh.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        twelfth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        thirteenth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        sfirst.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        ssecond.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        sthird.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        sfourth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        sfifth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        ssixth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        sseventh.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        seighth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        sninth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        stenth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        seleventh.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        stwelfth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        sthirteenth.backgroundTintList = getColorStateList(android.R.color.holo_purple)
        first.isClickable = false
        second.isClickable = false
        third.isClickable = false
        fourth.isClickable = false
        fifth.isClickable = false
        sixth.isClickable = false
        seventh.isClickable = false
        eighth.isClickable = false
        ninth.isClickable = false
        tenth.isClickable = false
        eleventh.isClickable = false
        twelfth.isClickable = false
        thirteenth.isClickable = false
        sfirst.isClickable = false
        ssecond.isClickable = false
        sthird.isClickable = false
        sfourth.isClickable = false
        sfifth.isClickable = false
        ssixth.isClickable = false
        sseventh.isClickable = false
        seighth.isClickable = false
        sninth.isClickable = false
        stenth.isClickable = false
        seleventh.isClickable = false
        stwelfth.isClickable = false
        sthirteenth.isClickable = false
        clearingDices()
        clearingOtherButtons()
    }
    // Function which shows how many throws player has
    private fun changingThrowsText() {
        var helpful = findViewById<TextView>(R.id.amountOfThrowsTxt)
        if(amountOfLeftThrows == 3) {
            helpful.text = "3 RZUTY"
        } else if(amountOfLeftThrows == 2) {
            helpful.text = "2 RZUTY"
        } else if(amountOfLeftThrows == 1) {
            helpful.text = "1 RZUT"
        } else {
            helpful.text = "BRAK RZUTÓW"
        }
    }
    // Helpful function which clear dices after choosing button
    @RequiresApi(Build.VERSION_CODES.M)
    private fun clearingDices() {
        val dice1Img: ImageButton = findViewById(R.id.imageButton1)
        val dice2Img: ImageButton = findViewById(R.id.imageButton2)
        val dice3Img: ImageButton = findViewById(R.id.imageButton3)
        val dice4Img: ImageButton = findViewById(R.id.imageButton4)
        val dice5Img: ImageButton = findViewById(R.id.imageButton5)
        dice1Img.setImageResource(0)
        dice2Img.setImageResource(0)
        dice3Img.setImageResource(0)
        dice4Img.setImageResource(0)
        dice5Img.setImageResource(0)
        dice1Img.backgroundTintList = getColorStateList(android.R.color.white)
        dice2Img.backgroundTintList = getColorStateList(android.R.color.white)
        dice3Img.backgroundTintList = getColorStateList(android.R.color.white)
        dice4Img.backgroundTintList = getColorStateList(android.R.color.white)
        dice5Img.backgroundTintList = getColorStateList(android.R.color.white)
        dice1Img.isClickable = false
        dice2Img.isClickable = false
        dice3Img.isClickable = false
        dice4Img.isClickable = false
        dice5Img.isClickable = false
    }
    // Rolling dices
    @RequiresApi(Build.VERSION_CODES.M)
    private fun rollDice() {
            val dice = Dice()
            if(currentFormat == "3 dices") {
                val roll2 = dice.roll(false)
                val roll3 = dice.roll(false)
                val roll4 = dice.roll(false)
                val dice2Img: ImageButton = findViewById(R.id.imageButton2)
                if(dice2Img.backgroundTintList == getColorStateList(android.R.color.white)) {
                    updateImg(roll2, dice2Img)
                    dices[1] = roll2
                }
                val dice3Img: ImageButton = findViewById(R.id.imageButton3)
                if(dice3Img.backgroundTintList == getColorStateList(android.R.color.white)) {
                    updateImg(roll3, dice3Img)
                    dices[2] = roll3
                }
                val dice4Img: ImageButton = findViewById(R.id.imageButton4)
                if(dice4Img.backgroundTintList == getColorStateList(android.R.color.white)) {
                    updateImg(roll4, dice4Img)
                    dices[3] = roll4
                }
                if(amountOfLeftThrows == 0) {
                    dice2Img.isClickable = false
                    dice3Img.isClickable = false
                    dice4Img.isClickable = false
                } else if (amountOfLeftThrows == 2 || amountOfLeftThrows == 1) {
                    dice2Img.isClickable = true
                    dice3Img.isClickable = true
                    dice4Img.isClickable = true
                    blockingDice(dice2Img)
                    blockingDice(dice3Img)
                    blockingDice(dice4Img)
                }
            } else {
                val roll1 = dice.roll()
                val roll2 = dice.roll(false)
                val roll3 = dice.roll(false)
                val roll4 = dice.roll(false)
                val roll5 = dice.roll(false)
                val dice1Img: ImageButton = findViewById(R.id.imageButton1)
                if(dice1Img.backgroundTintList == getColorStateList(android.R.color.white)) {
                    updateImg(roll1, dice1Img)
                    dices[0] = roll1
                }
                val dice2Img: ImageButton = findViewById(R.id.imageButton2)
                if(dice2Img.backgroundTintList == getColorStateList(android.R.color.white)) {
                    updateImg(roll2, dice2Img)
                    dices[1] = roll2
                }
                val dice3Img: ImageButton = findViewById(R.id.imageButton3)
                if(dice3Img.backgroundTintList == getColorStateList(android.R.color.white)) {
                    updateImg(roll3, dice3Img)
                    dices[2] = roll3
                }
                val dice4Img: ImageButton = findViewById(R.id.imageButton4)
                if(dice4Img.backgroundTintList == getColorStateList(android.R.color.white)) {
                    updateImg(roll4, dice4Img)
                    dices[3] = roll4
                }
                val dice5Img: ImageButton = findViewById(R.id.imageButton5)
                if(dice5Img.backgroundTintList == getColorStateList(android.R.color.white)) {
                    updateImg(roll5, dice5Img)
                    dices[4] = roll5
                }
                if(amountOfLeftThrows == 0) {
                    dice1Img.isClickable = false
                    dice2Img.isClickable = false
                    dice3Img.isClickable = false
                    dice4Img.isClickable = false
                    dice5Img.isClickable = false
                } else if (amountOfLeftThrows == 2 || amountOfLeftThrows == 1) {
                    dice1Img.isClickable = true
                    dice2Img.isClickable = true
                    dice3Img.isClickable = true
                    dice4Img.isClickable = true
                    dice5Img.isClickable = true
                    blockingDice(dice1Img)
                    blockingDice(dice2Img)
                    blockingDice(dice3Img)
                    blockingDice(dice4Img)
                    blockingDice(dice5Img)
                }
            }
    }
    // Blocking dices
    @RequiresApi(Build.VERSION_CODES.M)
    private fun blockingDice(b: ImageButton) {

        b.setOnClickListener {
            if (b.backgroundTintList == getColorStateList(android.R.color.white)) {
                b.backgroundTintList = getColorStateList(android.R.color.black)
            } else {
                b.backgroundTintList = getColorStateList(android.R.color.white)
            }
        }
    }
    // Update img of rolled dice
    private fun updateImg(roll: Int, diceImg: ImageButton) {
        diceImg.setImageResource(resolveDrawable(roll))
    }
    // Helpful function to choose image
    private fun resolveDrawable(value: Int): Int {
        return when (value) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.dice_1
        }
    }
    //Creating menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    // Choosing options from menu
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settingsItem -> startSettingsActivity()
            R.id.rulesItem -> startRulesActivity()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
    // Starting rules activity
    @RequiresApi(Build.VERSION_CODES.M)
    private fun startRulesActivity() {
        val intent = Intent(this,RulesActivity::class.java)
        startActivity(intent)
    }
}