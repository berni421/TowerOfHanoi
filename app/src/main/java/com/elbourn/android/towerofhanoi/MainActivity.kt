package com.elbourn.android.towerofhanoi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import com.elbourn.android.towerofhanoi.fragments.ProcessingFragment
import com.elbourn.android.towerofhanoi.fragments.ProcessingFragment.Companion.processingFragment


class MainActivity : OptionsMenu() {
    val TAG: String = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Handle back pressed
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            //showing dialog and then closing the application..
            finishAffinity()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        sketch.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        sketch.onNewIntent(intent)
    }

    companion object {
        var sketch = ProcessingFragment.sketch
    }
}