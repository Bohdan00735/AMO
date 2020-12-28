package com.example.amo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.*
import com.example.amo.backend.secondLab.Sorter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.Series
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class SecondLab : AppCompatActivity() {
    private lateinit var sorter: Sorter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_lab)
        sorter = Sorter(0, 0)
        val sort = findViewById<TextView>(R.id.sortedText)
        sort.movementMethod = ScrollingMovementMethod()
    }

    fun generateOnClick(view:View){
        val check = findViewById<CheckBox>(R.id.autoFillCheckSwecondLab)
        var min = 0
        var max = 0
        if (check.isChecked){
            max = 600
        }else{
            val minBorder = findViewById<EditText>(R.id.minBorder)
            val maxBorder = findViewById<EditText>(R.id.maxBorder)

            try {
                min = minBorder.text.toString().toInt()
                max = maxBorder.text.toString().toInt()
            }catch (e: Exception){
                val toast = Toast.makeText(applicationContext, "input something", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        if (min < max){
            generateAndOut(min,max)
        }else{
            val toast = Toast.makeText(applicationContext, "Error input", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun generateAndOut(min:Int, max:Int){
        val unsortOutput = findViewById<TextView>(R.id.unsortedText)
        val sortOutput = findViewById<TextView>(R.id.sortedText)
        sorter.min = min
        sorter.max = max
        val times = sorter.doAll()

        val unsortedArrays = sorter.matrix
        val sortedArrays = sorter.sorted
        val outputSorted = StringBuilder()
        val outputUnSorted = StringBuilder()

        outputSorted.append("Sorted").append("\n")
        outputUnSorted.append("Unsorted").append("\n")

        for (i in unsortedArrays.indices){
            outputSorted.append(sortedArrays[i].asList().toString()).append("\n")
            outputUnSorted.append(unsortedArrays[i].asList().toString()).append("\n")
        }
        unsortOutput.text = outputUnSorted
        sortOutput.text = outputSorted
        builtGraph(times);
    }

    private fun builtGraph(times:DoubleArray){
        val graphExperemental = findViewById<GraphView>(R.id.graph)
        val graphToeretical = findViewById<GraphView>(R.id.teorGraph)
        graphToeretical.title = "Graph of n^2"
        val points = Array<DataPoint>(11){DataPoint(0.0,0.0)}
        points[0] = DataPoint(0.0,0.0)
        points[1] = DataPoint(10.0, times[0]/30)
        for (i in 1 until 10){
            points[i+1] = (DataPoint((i+2).toDouble()*5,times[i]/5))
        }
        val series = LineGraphSeries<DataPoint>(points)
        graphExperemental.title="Experimental values"
        graphExperemental.gridLabelRenderer.gridColor = Color.WHITE
        graphExperemental.titleColor = Color.rgb(210, 180, 140)
        graphToeretical.titleColor = Color.rgb(210, 180, 140)
        graphExperemental.gridLabelRenderer.numHorizontalLabels = 30
        graphExperemental.removeAllSeries()
        series.color = Color.rgb(75, 0, 130)
        graphExperemental.addSeries(series)
        graphToeretical.gridLabelRenderer.gridColor = Color.WHITE
        graphToeretical.gridLabelRenderer.numHorizontalLabels = 30
        graphToeretical.addSeries(generateSpecialGraph())
    }

    fun generateSpecialGraph(): Series<*>? {
        val points = Array<DataPoint>(60){DataPoint(0.0,0.0)}
        for (i in 0 until 60){
            points[i] = DataPoint(i.toDouble(),i*i.toDouble())
        }
        val series = LineGraphSeries(points)
        series.color = Color.rgb(75, 0, 130)
        return series
    }

    fun autoFillCheck(view: View){
        val minBorder = findViewById<EditText>(R.id.minBorder)
        val maxBorder = findViewById<EditText>(R.id.maxBorder)
        val check = findViewById<CheckBox>(R.id.autoFillCheckSwecondLab)
        if (check.isChecked){
            minBorder.isFocusable = false
            maxBorder.isFocusable = false
        }else{
            minBorder.isFocusableInTouchMode = true
            maxBorder.isFocusableInTouchMode = true
        }
    }
}
