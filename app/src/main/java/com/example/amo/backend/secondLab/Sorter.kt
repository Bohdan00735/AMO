package com.example.amo.backend.secondLab

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class Sorter(var min: Int, var max: Int) {
    val matrix: Array<IntArray> = Array(10){IntArray(10)}
    val sorted: Array<IntArray> = Array(10){IntArray(10)}
    var times: LongArray = LongArray(10)

    fun doAll(): DoubleArray {
        generateTenArray()
        return sortAndCheck()
    }

    fun sort(arr: IntArray): IntArray {
        val result = arr.clone()
        for (i in result.size-1 downTo 0){
            var currIndex = i

            for (j in 0 until  currIndex){
                if (result[j] > result[currIndex]){currIndex = j}
            }
            val elem = result[currIndex]
            result[currIndex] = result[i]
            result[i] = elem

        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun generateArray(n: Int):IntArray{
        val result = IntArray(n)
        val random = java.util.Random()
        for (i in 0 until n){
            result[i] = (Math.random()*(max-min)+min).toInt()
        }
        return result
    }


    private fun generateTenArray(){
        for (i in 0 until  10){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                matrix[i] = generateArray((i+2)*5)
            }
        }
    }

    fun sortAndCheck(): DoubleArray {
        val timeCheck = LongArray(10)
        var resultTime = DoubleArray(10)
        for (i in 0 until 10){
            timeCheck[i] = System.nanoTime()
            sorted[i] = sort(matrix[i])
            timeCheck[i] = System.nanoTime()- timeCheck[i]
            resultTime[i] = timeCheck[i].toDouble()
        }
        times = timeCheck
        return resultTime
    }
}