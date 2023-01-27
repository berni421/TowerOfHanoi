package com.elbourn.android.towerofhanoi.processing

import android.util.Log
import processing.core.PApplet

class Towers {
    val TAG: String = javaClass.simpleName
    val columns = arrayListOf<Tower>()

    init {
        for (pos in Location.values()) {
            if (pos == Location.LEFT)
                columns.add(Tower(pos, numberOfDisks))
            else
                columns.add(Tower(pos, 0))
        }
    }

    fun display(pApplet: PApplet) {
        for (tower in columns) {
            Log.i(TAG, "Location: " + tower.pos)
            pApplet.push()
            val x = (1f + tower.pos.ordinal) * (pApplet.width / (1f + Location.values().size))
            val y = pApplet.height / 2 + Disk.diskHeight * numberOfDisks / 2
            pApplet.translate(x, y, 0f)
            pApplet.line(
                0f,
                0f,
                0f,
                0f,
                -Disk.diskHeight * (numberOfDisks + 1), 0f
            )
            pApplet.line(
                -Disk.diskBaseWidth * 3/2f,
                0f,
                0f,
                Disk.diskBaseWidth * 3/2f,
                0f,
                0f
            )
            tower.pile.display(pApplet)
            pApplet.pop()
        }
    }

    companion object {
        const val numberOfDisks: Int = 3
    }
}

class Tower(val pos: Location, number: Int) {
    var pile: Pile = Pile(number)
}

class Pile(number: Int) {
    val TAG: String = javaClass.simpleName
    val disks = arrayListOf<Disk>()

    init {
        for (i in 0 until number) {
            disks.add(Disk(i + 1))
        }
    }

    fun display(pApplet: PApplet) {
        if (disks.size == 0) {
            Log.i(TAG, "no disks")
            return
        }
        pApplet.push()
        for (disk in disks.reversed()) {
            Log.i(TAG, "disk size: " + disk.size)
            val x = 0f
            val y = -Disk.diskHeight
            pApplet.translate(x, y, 0f)
            disk.display(pApplet)
        }
        pApplet.pop()
    }

    fun addDisk(disk: Disk): Pile {
        val newPile = Pile(0)
        if (canAddDisk(disk.size)) {
            newPile.disks.add(disk)
            newPile.disks.addAll(disks)
        }
        return newPile
    }

    fun canAddDisk(size: Int): Boolean {
        var topDiskSize = Towers.numberOfDisks + 1 // a number larger than any other disks
        if (disks.size != 0) topDiskSize = disks[0].size
        return topDiskSize > size
    }

    fun canRemoveDisk(): Boolean {
        return (disks.size != 0)
    }

    fun getTopDisk(): Disk {
        return disks[0]
    }

    fun removeTopDisk(): Pile {
        val newDisks = Pile(0)
        newDisks.disks.addAll(disks.subList(1, disks.size))
        return newDisks
    }
}

class Disk(val size: Int) {
    fun display(pApplet: PApplet) {
        pApplet.push()
        pApplet.box(size.toFloat() * diskBaseWidth, diskHeight, 10f)
        pApplet.fill(Sketch.BLACK)
        pApplet.textSize(Sketch.textSize)
        pApplet.text(size.toString(),
            -pApplet.textWidth(size.toString())/2f,
            diskHeight/2 - Sketch.textSize/2f,
            10f)
        pApplet.pop()
    }

    companion object {
        const val diskHeight = 100f
        const val diskBaseWidth = 100f
    }
}

enum class Location(val pos: Int) {
    LEFT(0),
    CENTER(1),
    RIGHT(2)
}


