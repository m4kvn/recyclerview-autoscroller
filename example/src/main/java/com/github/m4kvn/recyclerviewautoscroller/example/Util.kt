package com.github.m4kvn.recyclerviewautoscroller.example

import android.graphics.Color

object Util {

    private val colorPatterns: Array<String> = arrayOf(
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"
    )

    fun getRandomColor(): Int {
        return Color.parseColor(mutableListOf<String>()
            .apply { repeat(6) { add(colorPatterns.random()) } }
            .joinToString(separator = "", prefix = "#"))
    }
}