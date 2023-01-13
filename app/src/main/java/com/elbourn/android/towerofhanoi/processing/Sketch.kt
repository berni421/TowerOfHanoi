package com.elbourn.android.towerofhanoi.processing

import android.util.Log
import kotlinx.coroutines.*
import processing.core.PApplet

/* https://en.wikipedia.org/wiki/Tower_of_Hanoi

The key to solving a problem recursively is to recognize that it can be broken down into a collection of smaller sub-problems, to each of which that same general solving procedure that we are seeking applies, and the total solution is then found in some simple way from those sub-problems' solutions. Each of these created sub-problems being "smaller" guarantees that the base case(s) will eventually be reached. Thence, for the Towers of Hanoi:

label the pegs A, B, C,
let n be the total number of disks,
number the disks from 1 (smallest, topmost) to n (largest, bottom-most).

Assuming all n disks are distributed in valid arrangements among the pegs; assuming there are m top disks on a source peg, and all the rest of the disks are larger than m, so they can be safely ignored; to move m disks from a source peg to a target peg using a spare peg, without violating the rules:

Move m − 1 disks from the source to the spare peg, by the same general solving procedure. Rules are not violated, by assumption. This leaves the disk m as a top disk on the source peg.
Move the disk m from the source to the target peg, which is guaranteed to be a valid move, by the assumptions — a simple step.
Move the m − 1 disks that we have just placed on the spare, from the spare to the target peg by the same general solving procedure, so they are placed on top of the disk m without violating the rules.
The base case is to move 0 disks (in steps 1 and 3), that is, do nothing – which obviously doesn't violate the rules.

The full Tower of Hanoi solution then consists of moving n disks from the source peg A to the target peg C, using B as the spare peg.

This approach can be given a rigorous mathematical proof with mathematical induction and is often used as an example of recursion when teaching programming.

 */

class Sketch : PApplet() {
    val TAG: String = javaClass.simpleName
    val permissions: Array<String>? =
        null // or {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CALENDAR};
    var toh = Towers()
    var msg = ""
    lateinit var moveJob: Job

    override fun settings() {
        size(displayWidth, displayHeight, P3D)
    }

    suspend fun job(future: Int) = withContext(Dispatchers.IO) {
        launch { // launch as parallel coroutine, but suspend
            delay(future)
            move(
                Towers.numberOfDisks,
                toh.columns[Location.LEFT.ordinal],
                toh.columns[Location.RIGHT.ordinal],
                toh.columns[Location.CENTER.ordinal]
            )
        }
    }

    fun startJobIn(future: Int): Job {
        return CoroutineScope(Dispatchers.Default).launch { job(future) }
    }

    fun jobDone(job: Job): Boolean {
        return job.isCompleted
    }

    fun jobRunning(job: Job): Boolean {
        return job.isActive
    }

    fun stopJob(job: Job) {
        if (jobRunning(job)) {
            job.cancel()
        }
    }

    override fun setup() {
        Log.i(TAG, "start setup")
        // Check permissions
        requestPermissions(permissions)
        background(BLACK)
        fill(WHITE)
        stroke(WHITE)
        strokeWeight(20f)
        textSize(textSize)
        toh = Towers()
        moveJob = startJobIn(2000)
        frameRate(2f)
        Log.i(TAG, "end setup")
    }

    fun move(
        n: Int,
        sourceTower: Tower,
        destinationTower: Tower,
        auxiliaryTower: Tower
    ) {
        if (n > 0) {
            move(n - 1, sourceTower, auxiliaryTower, destinationTower)
            moveTopDisk(sourceTower, destinationTower)
            move(n - 1, auxiliaryTower, destinationTower, sourceTower)
        }
    }


    fun moveTopDisk(sourceTower: Tower, destinationTower: Tower) {
        // move the top disk
        val disk = sourceTower.pile.getTopDisk()
        msg = "Moving disk: " + disk.size +
                " from " + sourceTower.pos.name +
                " to " + destinationTower.pos.name
        Log.i(TAG, msg)
        sourceTower.pile = sourceTower.pile.removeTopDisk()
        destinationTower.pile = destinationTower.pile.addDisk(disk)
        delay(2000)
    }

    override fun draw() {
        Log.i(TAG, "started draw")
        // Check permissions OK
        if (!hasPermissions(permissions)) return
        // Display current tower state
        background(BLACK)
        // Screen body
        drawBody()
        // Display current tower state
        toh.display(this)
//        Log.i(TAG, "frameRate: " + frameRate)
    }

    fun drawBody() {
        push()
        textAlign(CENTER)
        text("Towers of Hanoi", width / 2f, height * 0.25f)
        text(msg, width / 2f, height * 0.8f)
        // check if job done
        if (jobDone(moveJob)) {
            text("TAP to restart", width / 2f, height * 0.9f)
            noLoop()
        }
        pop()
    }

    private fun requestPermissions(permissions: Array<String>?) {
        if (permissions == null) return
        for (permission in permissions) {
            requestPermission(permission)
        }
    }

    private fun hasPermissions(permissions: Array<String>?): Boolean {
        if (permissions == null) return true
        var t = ""
        for (permission in permissions) {
            if (!hasPermission(permission)) {
                val p = permission.substring(permission.lastIndexOf('.') + 1)
                t = t + p + "\n"
            }
        }
        text(t + "permission required", (width / 2).toFloat(), (height / 2).toFloat())
        return t == ""
    }

    override fun touchStarted() {
        Log.i(TAG, "start touchStarted")
        if (!jobRunning(moveJob)) {
            toh = Towers()
            loop()
            moveJob = startJobIn(2000)
        }
    }

    override fun touchMoved() {
    }

    override fun touchEnded() {
    }

    companion object {
        val BLACK = 0
        val WHITE = 255
        val textSize = 64f
    }
}