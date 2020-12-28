package com.example.amo.backend

import kotlin.math.pow

class Checker {
    fun checkForLinear(a:Double, b:Double) : Boolean {
        if (a == 0.0 || b == 0.0) return false
        else if (b/a < -5) return false
        return true
    }

    fun checkForBruch(v: Double, x:Double, c:Double, h:Double, q: Double,i:Double, p: Double):Boolean{
        return (v>x*(-1) && c > h*(-1)) || (q.pow(i)>x*(-1) && p.pow(i) > h*(-1))
    }
}

