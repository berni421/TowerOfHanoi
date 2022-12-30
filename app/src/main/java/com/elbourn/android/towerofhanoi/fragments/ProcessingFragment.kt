package com.elbourn.android.towerofhanoi.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elbourn.android.towerofhanoi.R
import com.elbourn.android.towerofhanoi.processing.Sketch
import processing.android.PFragment
import processing.core.PApplet

class ProcessingFragment : Fragment() {
    val TAG: String = javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_processing, container, false)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "started onResume")
        processingFragment.setView(view?.findViewById(R.id.frameLayout01), activity)
        sketchVisible = true
    }

    companion object {
        val sketch: PApplet = Sketch()
        val processingFragment = PFragment(sketch)
        var sketchVisible = false
    }
}
