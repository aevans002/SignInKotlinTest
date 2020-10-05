package com.aevans.signinkotlinapp

import android.R.string
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

/*
*****************************************************************************
*****************************************************************************
*  Project: SignInAppAndroid
*  Author: Allan Evans
*  Date Created: 10/4/2020
*  Class: MainActivity.cs
*  Overview: Class for the initial view, displays the user list (if any are
*  saved) and welcomes the user
*
*****************************************************************************
*****************************************************************************
*/

class MainActivity : AppCompatActivity() {

    //private var adapter: ArrayAdapter<string>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // setSupportActionBar(findViewById(R.id.toolbar))

        //link basic buttons and textboxes
        var userListView: ListView = findViewById(R.id.userListView)
        var newEntryBtn: Button = findViewById(R.id.newEntryBtn)

        // get reference to button
        newEntryBtn = findViewById(R.id.newEntryBtn)
        // set on-click listener to go to sign in page
        newEntryBtn.setOnClickListener {
            val intent = Intent(this, CreationActivity::class.java)
            startActivity(intent);
        }
    }

    override fun onResume() {
        super.onResume()
        readEntries()
    }

    private fun readEntries() {
        var emptySpace: String = "  "
        //Reads saved file if it exists and populates listview
        val fileString: String = getString(R.string.file_path)
        var file = File(filesDir.absolutePath, fileString)

        var userListView: ListView = findViewById(R.id.userListView)
        if (file.exists()) {
            //If the file exists, it is read and add to the list
            var existingEntries = file.readText()
            var existingArray : MutableList<String> = existingEntries.split(getString(R.string.new_line)).toMutableList()
            existingArray.removeAll(listOf(""))  //Checking for empty entries

            for((i, element) in existingArray.withIndex()) {
                var splitArray = existingArray[i].split(getString(R.string.seperator))
                existingArray[i] = getString(R.string.user) + emptySpace + splitArray[0] + emptySpace + emptySpace + getString(R.string.password) + emptySpace + splitArray[1]
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, existingArray)
            userListView.adapter = adapter

        }
    }
}