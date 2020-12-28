package com.example.amo

import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs


class FifthLab : AppCompatActivity() {

    private val testValues = arrayOf(arrayOf("11","3","-1","15"),
        arrayOf("2","5","-5","-11"),
        arrayOf("1","1","1","1"))
    private val spinnerValues = arrayOf("2", "3", "4", "5","6","7","8")
    private val xRow = arrayOf("*x1","*x2","*x3","*x4","*x5","*x6","*x7","*x8")
    var editTextElem = ArrayList<ArrayList<EditText>>()
    var tableDimension = 2

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth_lab)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter(this, R.layout.my_spinner_row, spinnerValues);
        val testButton = findViewById<Button>(R.id.setTestFifth)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter


        val itemSelectedListener = object : OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int,id: Long
            ) {
                setTable(position+1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        spinner.onItemSelectedListener = itemSelectedListener

        setTable(spinner.selectedItemPosition + 3)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setTestValues(view: View){

        val tableLayout = findViewById<TableLayout>(R.id.tableFifthLab)
        tableLayout.removeAllViews()
        editTextElem.clear()
        tableDimension = 3

        for (i in 0..2){
            val row = TableRow(this)
            val rowArray = ArrayList<EditText>()
            for (j in 0..5){
                if (j%2 == 0){
                    val input = EditText(this)
                    input.setText(testValues[i][j/2])
                    input.id = Integer.parseInt(i.toString() + j.toString())
                    input.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
                    rowArray.add(input)
                    row.addView(input)
                }else{
                    val text = TextView(this,null,0,R.style.MyTextStyle)
                    text.id = Integer.parseInt(i.toString() + j.toString())
                    text.text = xRow[j/2]
                    row.addView(text)
                }
            }
            val equalSymbol = TextView(this,null,0,R.style.MyTextStyle)
            equalSymbol.text = " = "
            equalSymbol.id = i
            row.addView(equalSymbol)
            val resVal = EditText(this)
            resVal.id = Integer.parseInt(i.toString() + "10")
            resVal.setText(testValues[i][3])
            resVal.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
            row.addView(resVal)
            rowArray.add(resVal)
            editTextElem.add(rowArray)
            tableLayout.addView(row)
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun  setTable(dimension: Int){
        editTextElem.clear()
        tableDimension = dimension+1
        val tableLayout = findViewById<TableLayout>(R.id.tableFifthLab)
        tableLayout.removeAllViews()

        for (i in 0..dimension){
            val row = TableRow(this)
            val rowArray = ArrayList<EditText>()
            for (j in 0..dimension*2+1){
                if (j%2 == 0){
                    val input = EditText(this)
                    input.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
                    input.setText("0")
                    rowArray.add(input)
                    input.id = Integer.parseInt(i.toString() + j.toString())
                    row.addView(input)
                }else{
                    val text = TextView(this,null,0,R.style.MyTextStyle)
                    text.id = Integer.parseInt(i.toString() + j.toString())
                    text.text = xRow[j/2]
                    row.addView(text)
                }
            }
            val equalSymbol = TextView(this,null,0,R.style.MyTextStyle)
            equalSymbol.text = " = "
            equalSymbol.id = i
            row.addView(equalSymbol)
            val resVal = EditText(this)
            resVal.id = Integer.parseInt(i.toString() + "10")
            resVal.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
            resVal.setText("0")
            row.addView(resVal)
            rowArray.add(resVal)
            editTextElem.add(rowArray)
            tableLayout.addView(row)
        }
    }

    private fun getValues(): kotlin.Array<DoubleArray> {
        val result = Array(tableDimension){DoubleArray(tableDimension)}
        for(i in 0 until tableDimension){
            for (j in 0 until tableDimension){
                result[i][j] = editTextElem[i][j].text.toString().toDouble()
            }
        }
        return result
    }

    private fun getAnswers():DoubleArray{
        val answers = DoubleArray(tableDimension)
        for (i in 0 until tableDimension){
            answers[i] = editTextElem[i][tableDimension].text.toString().toDouble()
        }
        return answers
    }

    private fun calculateRoots(arguments:kotlin.Array<DoubleArray>,answers : DoubleArray):DoubleArray{

        val eps = epsCalc()

        val w = bestRelaxationParameterCalc(arguments, answers, tableDimension)

        val x = DoubleArray(tableDimension)

        var counter = 0
        var maxChange: Double
        do {
            maxChange = 0.0
            for (i in 0 until tableDimension) {
                var firstSum = 0.0
                for (j in 0 until i) {
                    firstSum += arguments[i][j] * x[j]
                }
                var secondSum = 0.0
                for (j in i + 1 until tableDimension) {
                    secondSum += arguments[i][j] * x[j]
                }
                val lastTerm = (1 - w) * x[i]
                val z: Double = answers[i] - firstSum - secondSum
                val temp: Double = w * z / arguments[i][i] + lastTerm
                maxChange = maxChange.coerceAtLeast(abs(x[i] - temp))
                x[i] = temp
            }
            counter++
        } while (maxChange > eps)

        return x
    }

    fun findTheRootsOfTheEquation(view: View){
        val resultView = findViewById<TextView>(R.id.resultForFifth)
        val values = getValues()
        val res = getAnswers()
        resultView.text = calculateRoots(values, res).contentToString()
    }

    private fun bestRelaxationParameterCalc(
        a: Array<DoubleArray>,
        b: DoubleArray,
        n: Int
    ): Double {
        var bestW = 1.0
        var bestMaxChange = Double.MAX_VALUE
        var w = 0.05
        while (w <= 2) {
            val x = DoubleArray(n)

            var maxChange = 0.0
            for (s in 0 until 4) {
                maxChange = 0.0
                for (i in 0 until n) {
                    var firstSum = 0.0
                    for (j in 0 until i) {
                        firstSum += a[i][j] * x[j]
                    }
                    var secondSum = 0.0
                    for (j in i+1 until n) {
                        secondSum += a[i][j] * x[j]
                    }
                    val lastTerm = (1 - w) * x[i]
                    val z = b[i] - firstSum - secondSum
                    val temp = w * z / a[i][i] + lastTerm
                    maxChange = maxChange.coerceAtLeast(abs(x[i] - temp))
                    x[i] = temp
                }
            }
            if (maxChange < bestMaxChange) {
                bestMaxChange = maxChange
                bestW = w
            }
            w += 0.05
        }
        return bestW
    }

    private fun epsCalc(): Double {
        var eps = 1.0
        while (1 + eps > 1) {
            eps /= 2.0
        }
        return eps
    }
}
