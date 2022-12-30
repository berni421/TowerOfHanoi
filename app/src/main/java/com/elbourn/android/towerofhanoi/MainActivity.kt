package com.elbourn.android.towerofhanoi

import android.content.Intent
import android.os.Bundle
import com.elbourn.android.towerofhanoi.fragments.ProcessingFragment

class MainActivity : OptionsMenu() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
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