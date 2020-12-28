package com.example.amo.backend

import java.lang.Exception
import java.lang.Math.pow
import kotlin.math.pow
import kotlin.math.sqrt


class FirstLabAlgorithms{
    fun calculateLinearAlg(a:Double, b:Double): Double {
        return (a/b-5)/2+ sqrt((b/a+5)/3)
    }

    fun calculateBrunchAlg(c: Double,d: Double,i: Double,h:Double,p:Double,q:Double,v:Double,x: Double):Double {
        return if (i%3 == 0.0) q.pow(i)*d/sqrt(v+x)+p.pow(i)*d/sqrt(c+h)
        else p.pow(i)*d/sqrt(v+x)+q.pow(i)*d/sqrt(c+h)

    }

    fun  calculateCycleAlg(n:Int):Double{
        var result = 1.0
        var a = 1.0
        while(a <= n){
            for (b in 1..n+1){
                if (a == b.toDouble()) continue//skip because there will be 0 in denominator
                result *= (a.pow(4.0) + b.toDouble().pow(4.0))/
                          (a.pow(4.0) - b.toDouble().pow(4.0))
            }
            a+=0.25
        }
        return result;
    }

}
