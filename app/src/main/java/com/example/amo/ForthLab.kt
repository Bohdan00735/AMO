package com.example.amo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.example.amo.backend.MyChordMethod
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_second_lab.view.*
import java.lang.Exception
import java.util.*

class ForthLab : AppCompatActivity() {
    lateinit var myChordMethod: MyChordMethod

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forth_lab)
        myChordMethod = MyChordMethod()
    }

    fun getBorders(): Array<Double> {
        val first = findViewById<RadioButton>(R.id.firstBorders)
        val second = findViewById<RadioButton>(R.id.secondBorders)

        if (first.isChecked){
            return arrayOf(0.12,3.0)
        }else if (second.isChecked){
            return arrayOf(0.7,5.5)
        }

        return arrayOf(getInput(findViewById(R.id.aBorderInput)),getInput(findViewById(R.id.bBorderInput)))
    }

    private fun getInput(editText: EditText):Double{
        var result = -1.0
        try {
            result = editText.text.toString().toDouble()
        }catch (e: Exception){
            val toast = Toast.makeText(applicationContext, "input something in " + editText.hint, Toast.LENGTH_SHORT)
            toast.show()
        };return result
    }

    fun findRoots(view: View){
        val a =  getBorders()[0]
        val b =  getBorders()[1]
        val accuracy = getInput(findViewById(R.id.accuracyInput))

        buildFuncGraph(0.05,6.0)

        if (myChordMethod.checkBorders(a,b)){
            if (accuracy < 1 && accuracy != -1.0){

                val result = "Value calculated with accuracy = " + accuracy + " = "+
                        myChordMethod.calculateApproximateValueInBorder(a,b,accuracy) +
                        " in " + myChordMethod.numOfIterations + " iterations"
                findViewById<TextView>(R.id.resultOutputForthLab).text = result
                buildGraphWithHords(a,b)

            }else{
                val toast = Toast.makeText(applicationContext,
                    "accuracy should be lower 1", Toast.LENGTH_LONG)
                toast.show()
            }
        }else{
            val toast = Toast.makeText(applicationContext,
                "change borders", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    private fun buildGraphWithHords(a: Double, b: Double){
        val graphView = findViewById<GraphView>(R.id.grapOfFunctionWithHords)
        graphView.gridLabelRenderer.gridColor = Color.WHITE
        graphView.removeAllSeries()
        graphView.gridLabelRenderer.numHorizontalLabels = 7
        graphView.title = "Function with chords"
        buildGraph(graphView, myChordMethod.mainFunctionPoints(a,b+1),
            Array(1){Color.rgb(124,252,0)})
        buildGraph(graphView, myChordMethod.chords.toArray(arrayOfNulls(myChordMethod.chords.size)),
            Array(myChordMethod.chords.size){Color.rgb(128,0,0)})
    }

    private fun buildFuncGraph(a: Double, b: Double) {
        val graphView = findViewById<GraphView>(R.id.grapOfFunction)
        graphView.gridLabelRenderer.gridColor = Color.WHITE
        val colors = arrayOf(Color.rgb(255, 255, 0),
            Color.rgb(0,255,255),Color.rgb(124,252,0))
        graphView.setFadingEdgeLength(5)
        graphView.title = "Function and separated arg func"
        graphView.gridLabelRenderer.numHorizontalLabels = 4
        graphView.gridLabelRenderer.numVerticalLabels = 4
        graphView.gridLabelRenderer.isHighlightZeroLines = true
        buildGraph(graphView, myChordMethod.makePointOfSeparetedFunc(a,b), colors)
    }

    private fun buildGraph(graphView: GraphView, points:Array<Array<DataPoint>>, colors:Array<Int>){
        for (raw in points.indices) {
            val series = LineGraphSeries(points[raw])
            series.color = colors[raw]
            graphView.addSeries(series)
        }
    }

    fun radioButtonChoose(view: View){
        val first = findViewById<RadioButton>(R.id.firstBorders)
        val second = findViewById<RadioButton>(R.id.secondBorders)
        val aInput = findViewById<EditText>(R.id.aBorderInput)
        val bInput = findViewById<EditText>(R.id.bBorderInput)
        if (first.isChecked || second.isChecked){
            aInput.isFocusable = false
            bInput.isFocusable = false
        }else{
            aInput.isFocusableInTouchMode = true
            bInput.isFocusableInTouchMode = true
        }
    }
}
