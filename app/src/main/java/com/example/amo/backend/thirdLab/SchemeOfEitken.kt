package com.example.amo.backend.thirdLab

import com.jjoe64.graphview.series.DataPoint
import java.math.BigDecimal
import kotlin.math.*

class SchemeOfEitken(var x0: Double,var n: Int) {
    lateinit var xValues: DoubleArray
    lateinit var yValues: Array<BigDecimal>
    lateinit var aValues: Array<Array<BigDecimal>>


    private fun fillXValues(){
        xValues = DoubleArray(n+2)
        for (i in xValues.indices){
            xValues[i] = i/n.toDouble()
        }
    }

    fun fillYValuesMainFunc(){
        fillXValues()
        yValues = Array(xValues.size){ BigDecimal(0) }
        for (i in yValues.indices){
            yValues[i] = BigDecimal(cos(xValues[i]).pow(2))
        }
    }

    fun fillYValuesTestFunc(){
        fillXValues()
        yValues = Array(xValues.size){ BigDecimal(0) }
        for (i in yValues.indices){
            yValues[i] = BigDecimal(sin(xValues[i]))
        }
    }

    private fun findRow(x0:Double):Int{
        for (i in 0 until xValues.size-1) {
            if (x0 >= xValues[i] && x0 < xValues[i + 1]) {return i}
        };return xValues.size
    }

    fun calculateInterpolationPolynomials(){
        aValues = Array(n){ Array(n) {BigDecimal(0)}}
        var k= n
        for (j in 0 until n){
            for (i in 0 until k){
                if (j == 0){
                    aValues[i][j] = (((BigDecimal(x0 - xValues[i]) *yValues[i+1])-
                            (BigDecimal(x0-xValues[i+1]) *yValues[i]))/
                            BigDecimal(xValues[i+1]-xValues[i]))
                }else{
                    aValues[i][j] = (((BigDecimal(x0 - xValues[i])*aValues[i+1][j-1])-
                            (BigDecimal(x0-xValues[i+j+1]) *aValues[i][j-1]))/
                            BigDecimal(xValues[i+j+1]-xValues[i]))
                }
            }
            k--
        }
    }

    private fun findResult(raw:Int): BigDecimal{

        var biggestResult = (aValues[raw][1] - aValues[raw][0]).abs()
        for (i in 1 until n-1){
            val intermediateResult = (aValues[raw][i+1] - aValues[raw][i]).abs()
            if (intermediateResult.compareTo(biggestResult) == -1){
                biggestResult = intermediateResult
            }else{return if (aValues[raw][i].toInt() == 0) aValues[raw][0] else aValues[raw][i]}
        };return findLastNotNull(raw)
    }

    private fun findLastNotNull(raw: Int):BigDecimal{
        for (i in aValues[raw].indices){
            if (aValues[raw][i] == BigDecimal(0)){
                return aValues[raw][i-1]
            }
        };return aValues[raw][n-1]
    }

    private fun findLastndex(raw: Int):Int{
        for (i in aValues[raw].indices){
            if (aValues[raw][i] == BigDecimal(0)){
                return i-1
            }
        };return aValues[raw].size-1
    }


    fun mainAlgorithm():BigDecimal{
        calculateInterpolationPolynomials()
        return findResult(findRow(x0))
    }

    fun makeTableValues():Array<Array<BigDecimal>>{

        val raw = findRow(x0)
        val table = Array(findLastndex(raw)){Array(4){BigDecimal(0)} }
        for (i in table.indices){
            table[i][0] = BigDecimal(i + 1.0)
            table[i][1] = (aValues[raw][i].minus(aValues[raw][i+1]))
            table[i][2] = (aValues[raw][i].minus(real(xValues[raw]))).div(BigDecimal(10.0.pow(i)))
            table[i][3] = BigDecimal(1)- table [i][2].div(table[i][1])
        };return table
    }

    fun real(x:Double):BigDecimal{
        return BigDecimal(cos(x).pow(2))
    }

    fun generateGraph():Array<Array<DataPoint>> {
        calculateInterpolationPolynomials()
        val result = Array(aValues[0].size){Array(n){DataPoint(0.0,0.0)}}
        for (j in result.indices){
            x0 = (xValues[j+1] - xValues[1])/(xValues[2] - xValues[1])
            calculateInterpolationPolynomials()
            for (i in 0 until n-1){
                val y = -log10((aValues[1][i] - aValues[1][i+1]).abs().toFloat())
                result[i][j] = DataPoint(x0, y.absoluteValue.toDouble())
            }
        };return result
    }

    fun makeFuctionAndIntepolationPoints(): Array<Array<DataPoint>> {
        calculateInterpolationPolynomials()
        val result = Array(2){Array(n){DataPoint(0.0,0.0)}}
        for (i in result[0].indices){
            result[0][i] = DataPoint(xValues[i], yValues[i].toDouble())
            result[1][i] = DataPoint(xValues[i], findLastNotNull(findRow(xValues[i])).toDouble())
        };return result
    }
}