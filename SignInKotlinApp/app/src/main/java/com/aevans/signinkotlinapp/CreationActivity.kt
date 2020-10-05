package com.aevans.signinkotlinapp

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import java.io.File

/*
*****************************************************************************
*****************************************************************************
*  Project: SignInAppAndroid
*  Author: Allan Evans
*  Date Created: 10/4/2020
*  Class: CreationActivity.cs
*  Overview: Adds new user info with password and checks so that there are not
*  repeated users or repeated characters in the password
*
*****************************************************************************
*****************************************************************************
*/

class CreationActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation)

        //link basic buttons and textboxes
        var headerTxt: TextView = findViewById(R.id.textView1)
        var usertTxtBx: EditText = findViewById(R.id.userTxtBx)
        var passwordTxtBx: EditText = findViewById(R.id.passwordTxtBx)
        var createBtn: Button = findViewById(R.id.createBtn)

        createBtn.setOnClickListener {
            createAccountClick()
        }
    }

        private fun createAccountClick() {
            var headerTxt: TextView = findViewById(R.id.textView1)
            var userTxtBx: EditText = findViewById(R.id.userTxtBx)
            var passwordTxtBx: EditText = findViewById(R.id.passwordTxtBx)

            if (userTxtBx.length() == 0)
            {
                Toast.makeText(this, getText(R.string.name_short), Toast.LENGTH_SHORT).show()
                return
            }

            //Due to the info being saved on a simple text file certain characters are avoided
            if (userTxtBx.text.equals(getString(R.string.seperator)) || userTxtBx.text.equals(getString(R.string.backslash)))
            {
                Toast.makeText(this, getText(R.string.forbidden_char), Toast.LENGTH_SHORT).show()
                return
            }

            if (passwordTxtBx.text.equals(getString(R.string.seperator)) || passwordTxtBx.text.equals(getString(R.string.backslash)))
            {
                Toast.makeText(this, getText(R.string.forbidden_char), Toast.LENGTH_SHORT).show()
                return
            }

            //Make things consistent for username and password, so they don't become so large they clip the list view
            if (passwordTxtBx.length() < 5) {
                Toast.makeText(this, getText(R.string.password_short), Toast.LENGTH_SHORT).show()
                return
            }

            //Keep the user name and password to a certain limit to avoid clipping
            if (passwordTxtBx.length() > 12 || userTxtBx.length() > 24) {
                Toast.makeText(this, getText(R.string.password_long), Toast.LENGTH_SHORT).show()
                return
            }

            //Check if the password is all numbers or all characters
            if (TextUtils.isDigitsOnly(passwordTxtBx.text)) {
                Toast.makeText(this, getText(R.string.password_mix), Toast.LENGTH_SHORT).show()
                return
            }
            var currentNum = 0
            for (littleChar in passwordTxtBx.text) {
                if (littleChar.isDigit()) {
                    currentNum ++
                }
            }
            if (currentNum == 0) {
                Toast.makeText(this, getText(R.string.password_mix), Toast.LENGTH_SHORT).show()
                return
            }
            //Put password into CharArray to check for repeating characters
            val charSplit = passwordTxtBx.text.toString().toCharArray()
            //Old fashion for statement
            for((i, element) in charSplit.withIndex()) {
                if (i < charSplit.count() - 3) {
                    if (charSplit[i] == charSplit[i + 1] && charSplit[i + 1] == charSplit[i + 2]) { //aaa
                        Toast.makeText(this, getText(R.string.password_repeat), Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                if (i < charSplit.count() - 2) {
                    if (charSplit[i] == charSplit[i + 1]) { //aa
                        Toast.makeText(this, getText(R.string.password_repeat), Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                if (i < charSplit.count() - 4) {  //Need to combine strings instead of char
                    if ((charSplit[i].toString() + charSplit[i + 1].toString()) == (charSplit[i + 2].toString() + charSplit[i + 3].toString())) {  //abab
                        Toast.makeText(this, getText(R.string.password_repeat), Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                if (charSplit.count() >= 6 && i < charSplit.count() - 6) {
                    if ((charSplit[i].toString() + charSplit[i + 1].toString() + charSplit[i + 2].toString()) == (charSplit[i + 3].toString() + charSplit[i + 4].toString() + charSplit[i + 5].toString())) { //abcabc
                        Toast.makeText(this, getText(R.string.password_repeat), Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }
            saveNewEntry(userTxtBx.text.toString(), passwordTxtBx.text.toString())
        }

        private fun saveNewEntry(user:String, password:String) {
            //Init an empty string
            var newEntry = ""
            //Reads saved file if it exists and populates listview
            val fileString: String = getString(R.string.file_path)
            var file = File(filesDir.absolutePath, fileString)
            if (file.exists()) {
                newEntry = file.readText()
                var oldArray = newEntry.split(getString(R.string.new_line))
                for((i, element) in oldArray.withIndex()) {
                    //Need to add array of prohibited chars later
                    var splitOld = oldArray[i].split(getString(R.string.seperator))
                    if (splitOld[0] == user) {  //Can have same password but not the same user
                        Toast.makeText(this, getText(R.string.entry_exists), Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }
            newEntry += user + getString(R.string.seperator) + password + getString(R.string.new_line) //Put in check to prevent same entry multiple times
            file.writeText(newEntry)  //Easy way to write text file??
            Toast.makeText(this, getText(R.string.entry_created), Toast.LENGTH_SHORT).show()
        }
    }
