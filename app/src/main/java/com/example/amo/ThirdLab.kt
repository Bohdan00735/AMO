package com.example.amo

import android.content.Context
import android.graphics.Color
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.view.get
import com.example.amo.backend.thirdLab.SchemeOfEitken
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.lang.Exception
import java.math.BigDecimal
import java.math.MathContext

class ThirdLab : AppCompatActivity() {
    private lateinit var schemeOfEitken: SchemeOfEitken
    private lateinit var specialSchemeOfEitken: SchemeOfEitken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_lab)

        specialSchemeOfEitken = SchemeOfEitken(0.5, 11)
    }

    fun calclulateInterpolation(view: View){
        val x0Text = findViewById<EditText>(R.id.x0InputThirdLab)

        var x0 = 0.0
        try {
            x0 = x0Text.text.toString().toDouble()
        }catch (e: Exception){
            val toast = Toast.makeText(applicationContext, "input something", Toast.LENGTH_SHORT)
            toast.show()
        }


        val n = checkFromNInput()

        if (n != 0){
            if(x0 > 1.0 || x0 < 0.0){
                val toast = Toast.makeText(applicationContext,
                    "x0 should to be placed in the interval [0;1]", Toast.LENGTH_LONG)
                toast.show()
            }else{
                val resultView = findViewById<TextView>(R.id.resultThirdLab)
                schemeOfEitken = SchemeOfEitken(x0,11)
                schemeOfEitken.fillYValuesMainFunc()
                val mathContext = MathContext(6)
                val output = "Result : " + schemeOfEitken.mainAlgorithm().abs(mathContext)
                resultView.text = output
                setGraphOfInterpolationSweep(n)
                setGraphOfFunction(x0, n)
                setTableVar()
            }
        }
    }

    private fun checkFromNInput(): Int {
        val inputN = findViewById<EditText>(R.id.nInputThirdLab)
        val check = findViewById<CheckBox>(R.id.autoNThirdLab)
        if (check.isChecked) return 10
        var n = 0

        try {
            n = inputN.text.toString().toInt()
        }catch (e: Exception){
            val toast = Toast.makeText(applicationContext, "input something", Toast.LENGTH_SHORT)
            toast.show()
        }


        if (n !in 10..30){
            val toast = Toast.makeText(applicationContext,
                "input N from the interval [10;30]", Toast.LENGTH_LONG)
            toast.show()
            return 0
        };return n
    }

    private fun setGraphOfFunction(x0:Double, n:Int){
        specialSchemeOfEitken = SchemeOfEitken(x0,n)
        specialSchemeOfEitken.fillYValuesMainFunc()
        val graphView = findViewById<GraphView>(R.id.graphInterpolation)
        graphView.gridLabelRenderer.gridColor = Color.WHITE
        buildGraph(graphView, specialSchemeOfEitken.makeFuctionAndIntepolationPoints())
    }


    private fun setGraphOfInterpolationSweep(n:Int){
        specialSchemeOfEitken = SchemeOfEitken(0.5, n)
        specialSchemeOfEitken.fillYValuesMainFunc()
        val graphView = findViewById<GraphView>(R.id.secondGraph)
        graphView.title=""
        graphView.gridLabelRenderer.gridColor = Color.WHITE
        graphView.titleColor = Color.rgb(210, 180, 140)
        buildGraph(graphView, specialSchemeOfEitken.generateGraph())
    }

    private fun buildGraph(graphView: GraphView, points:Array<Array<DataPoint>>){
        graphView.removeAllSeries()
        for (raw in points) {
            val series = LineGraphSeries(raw)
            series.color = Color.rgb(75, 0, 130)
            graphView.addSeries(series)
        }
    }

    private fun setTableVar(){
        schemeOfEitken.x0 = 0.05
        schemeOfEitken.calculateInterpolationPolynomials()
        val table = schemeOfEitken.makeTableValues()
        for (row in table){
            addRow(row)
        }
    }

    private fun addRow(raw:Array<BigDecimal>){
        val tableView = findViewById<TableLayout>(R.id.tableThirdLab)
        val inflater = LayoutInflater.from(this)
        val tableRow = inflater.inflate(R.layout.teble_row_third_lab, null)
        var columnView = tableRow.findViewById<TextView>(R.id.col1)
        val mathContext = MathContext(2)

        columnView.text = raw[0].toInt().toString()
        columnView = tableRow.findViewById(R.id.col2)
        columnView.text = raw[1].abs(mathContext).toString()
        columnView = tableRow.findViewById(R.id.col3)
        columnView.text = raw[2].abs(mathContext).toString()
        columnView = tableRow.findViewById(R.id.col4)
        columnView.text = raw[3].abs(mathContext).toString()

        tableView.addView(tableRow)
    }

    fun onCheck(view: View){
        val inputN = findViewById<EditText>(R.id.nInputThirdLab)
        val check = findViewById<CheckBox>(R.id.autoNThirdLab)
        if (check.isChecked){
            inputN.isFocusable = false
        }else{
            inputN.isFocusableInTouchMode = true
        }
    }

}
