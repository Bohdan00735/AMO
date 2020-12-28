package com.example.amo.backend

import com.jjoe64.graphview.series.DataPoint
import java.math.BigDecimal
import kotlin.math.absoluteValue
import kotlin.math.log10

class MyChordMethod {
    var numOfIterations = 0
    lateinit var chords: ArrayList<Array<DataPoint>>

    fun calculateMyFun(x:Double):Double{
        return 2*log10(x) - x/2 + 1
    }

    fun checkBorders(a:Double, b: Double):Boolean{
        if(a <= 0 || b <= 0) return false
        return calculateMyFun(a)*calculateMyFun(b) < 0 && a < b
    }

    fun calculateApproximateValueInBorder(a: Double, b: Double, accuracy: Double): Double {
        var aBorder = a
        var bBorder = b
        chords = ArrayList()
        numOfIterations = 0
        var move = true
        if (b < 4.681){
            aBorder = b
            bBorder = a
            move = false
        }
        while (true){
            val prev = aBorder
            val result = aBorder - calculateMyFun(aBorder)*(bBorder-aBorder)/
                    (calculateMyFun(bBorder) - calculateMyFun(aBorder))
            numOfIterations++
            if((result-aBorder).absoluteValue < accuracy) return result

            if (move){addChord(aBorder,bBorder,prev,b)
            }else{addChord(aBorder,bBorder,a,prev)}

            aBorder = result
        }
    }

    fun makePointOfSeparetedFunc(a:Double, b: Double):Array<Array<DataPoint>>{
        val result = Array(3){Array(50){DataPoint(0.0,0.0)} }
        for (i in result[0].indices){
            val x = (i/result[0].size.toDouble())*(b-a)+a
            result[0][i] = DataPoint(x ,2* log10(x)+1)
            result[1][i] = DataPoint(x,x/2)
            result[2][i] = DataPoint(x, calculateMyFun(x))
        }
        return result
    }


    private fun addChord(x1: Double, x2: Double, a: Double, b: Double) {
        val y1 = calculateMyFun(x1)
        val y2 = calculateMyFun(x2)

        val chord = Array(50){DataPoint(0.0,0.0)}
        for (i in chord.indices){
            val xi = i/chord.size.toDouble()*(b-a)+a
            val yi = (xi-x1)*(y2-y1)/(x2-x1)+y1
            chord[i] = DataPoint(xi,yi)
        }
        chords.add(chord)
    }

    fun mainFunctionPoints(a: Double, b: Double): Array<Array<DataPoint>> {
        val points = Array(1){Array(50){DataPoint(0.0,0.0)}}
        for (i in points[0].indices){
            val xi = i/points[0].size.toDouble()*(b+0.3-a)+a
            points[0][i] = DataPoint(xi,calculateMyFun(xi))
        }
        return points
    }
}