package com.example.buylist

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var isRunningWeb = false
    private var isFirstStartApp = true
    private val WEB_BOOLEAN = "WEB_BOOLEAN"
    private val START_BOOLEAN = "START_BOOLEAN"
    private val PREFERENCES = "PREFERENCES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences: SharedPreferences =
            getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

        isFirstStartApp = preferences.getBoolean(START_BOOLEAN, isFirstStartApp)
        isRunningWeb = preferences.getBoolean(WEB_BOOLEAN, isRunningWeb)

        if (isFirstStartApp) {
            isFirstStartApp = false
            if (isNetworkAvailable()) {
                isRunningWeb = true
                goWeb(savedInstanceState)
            } else {
                isRunningWeb = false
                goToBuyList(savedInstanceState)
            }
        } else {
            if (isRunningWeb) {
                goWeb(savedInstanceState)
            } else {
                goToBuyList(savedInstanceState)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onSaveData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        onSaveData()
    }

    private fun goToBuyList(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, BuyListFragment())
                .commit()
        }
    }

    private fun goWeb(savedInstanceState: Bundle?){
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, WebFragment())
                .commit()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun onSaveData() {
        val preferences: SharedPreferences =
            getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean(WEB_BOOLEAN,isRunningWeb)
        editor.putBoolean(START_BOOLEAN,isFirstStartApp)
        editor.apply()
    }
}