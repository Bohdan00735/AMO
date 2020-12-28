package com.example.amo

import android.content.Context
import android.database.CharArrayBuffer
import android.os.Bundle
import android.os.Environment
import android.text.InputFilter
import android.view.View
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.amo.backend.Checker
import com.example.amo.backend.FirstLabAlgorithms
import java.io.*
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.StringBuilder
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.math.sqrt

class FirstLab : AppCompatActivity() {
    private lateinit var calculator : FirstLabAlgorithms
    private lateinit var checker: Checker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_lab)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        calculator = FirstLabAlgorithms()
        checker = Checker()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun makeCalculationLinear(view: View){
        val numAText = findViewById<EditText>(R.id.enterValueA)
        val numBText = findViewById<EditText>(R.id.enterValueB)
        try {
            val numA = numAText.text.toString().toDouble()
            val numB = numBText.text.toString().toDouble()
            val resultLinear = findViewById<TextView>(R.id.LinearResult)

            if (checker.checkForLinear(numA, numB)){
                resultLinear.text = calculator.calculateLinearAlg(numA,numB).toString()
                saveToFile(numA.toString() + "\n" + numB + "\n", "FirstLabLinearFile.txt" )
            }else{
                val toast = Toast.makeText(applicationContext, "Invalid input, look for algorithm", Toast.LENGTH_LONG)
                toast.show()
            }
        }catch (e:Exception){
            val toast = Toast.makeText(applicationContext, "input something", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    fun calculateBranchles(view: View){
        val numQText = findViewById<EditText>(R.id.inputQ)
        val numDText = findViewById<EditText>(R.id.inputD)
        val numVText = findViewById<EditText>(R.id.inputV)
        val numPText = findViewById<EditText>(R.id.inputP)
        val numCText = findViewById<EditText>(R.id.inputC)
        val numHText = findViewById<EditText>(R.id.inputH)
        val numIText = findViewById<EditText>(R.id.inputI)
        val numXText = findViewById<EditText>(R.id.inputX)

        try {
            val numQ = numQText.text.toString().toDouble()
            val numD = numDText.text.toString().toDouble()
            val numV = numVText.text.toString().toDouble()
            val numP = numPText.text.toString().toDouble()
            val numC = numCText.text.toString().toDouble()
            val numH = numHText.text.toString().toDouble()
            val numI = numIText.text.toString().toDouble()
            val numX = numXText.text.toString().toDouble()

            val result = findViewById<TextView>(R.id.ResultBrunchles)

            if (checker.checkForBruch(numV,numX,numC,numH,numQ,numI,numP)){
                result.text = calculator.calculateBrunchAlg(numC,numD,numI,numH,numP,numQ,numV,numX).toString()
                saveToFile(numC.toString() + "\n" + numD.toString() + "\n" + numI + "\n"+
                        numH + "\n" + numP + "\n"+numQ + "\n"+numV + "\n"+numX + "\n", "FirstLabBrunch.txt" )
            }else{
                val toast = Toast.makeText(applicationContext, "Invalid input, look for algorithm", Toast.LENGTH_LONG)
                toast.show()
            }
        }catch (e:Exception){
            val toast = Toast.makeText(applicationContext, "input something", Toast.LENGTH_LONG)
            toast.show()
        }

    }

    fun calculateCycle(view: View){
        val numNText = findViewById<EditText>(R.id.inputValueN)
        val checkInputCycle = findViewById<CheckBox>(R.id.inputValueCheck)
        val checkRandom = findViewById<CheckBox>(R.id.generateRandomCheck)
        val toWrite = findViewById<TextView>(R.id.resultCycle)
        var result = StringBuilder()
        if(checkInputCycle.isChecked){
            try {
                val numN = numNText.text.toString().toInt()
                if (numN > 100){
                    val toast = Toast.makeText(applicationContext, "input lower num", Toast.LENGTH_LONG)
                    toast.show()
                }else{
                    result.append("Input result: ").append(calculator.calculateCycleAlg(numN).toString()).append("\n")
                    saveToFile("$numN,", "FirstLabCycleFile" )
                }
            }catch (e:Exception){
                val toast = Toast.makeText(applicationContext, "input something", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        if (checkRandom.isChecked){
            val  random = Random()
            val n = random.nextInt(10)
            result.append("Random cycles: ").append(n).append(" result: ")
                .append(calculator.calculateCycleAlg(n).toString()).append("\n")
        }


        toWrite.setText(result)
    }


    fun saveToFile(data : String, file : String){
        try {
            val outputfile = openFileOutput(file, Context.MODE_APPEND)
            val outputStreamWriter = ObjectOutputStream(outputfile)
            outputStreamWriter.writeObject(data)
            outputStreamWriter.flush()
            outputStreamWriter.close()
        }catch (e:Exception){e.printStackTrace()}

    }

    fun getFromFile(file: String, size: Int): String {
        try {
            val nums = CharArray(size)
            val inputStream =  openFileInput(file)
            val inputStreamReader = InputStreamReader(inputStream)
            inputStreamReader.read(nums)
            return String(nums)
        }catch (e:IOException){
            e.printStackTrace()
            val toast = Toast.makeText(applicationContext, "No such file or it`s empty", Toast.LENGTH_LONG)
            toast.show()
            return " "
        }
    }

    fun getForLinear(view: View){
        try {
            val parameters = getFromFile("FirstLabLinearFile.txt",500)

            val splited = parameters.split("\n")
            val numAText = findViewById<EditText>(R.id.enterValueA)
            val numBText = findViewById<EditText>(R.id.enterValueB)
            numAText.setText(splited[splited.size-3])
            numBText.setText(splited[splited.size-2])

        }catch (e : NullPointerException){
            val toast = Toast.makeText(applicationContext, "File is empty", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    fun getForBruncles(view: View){
        try {
            val parameters = getFromFile("FirstLabBrunch.txt",500)

            val splited = parameters.split("\n")
            val numQText = findViewById<EditText>(R.id.inputQ)
            val numDText = findViewById<EditText>(R.id.inputD)
            val numVText = findViewById<EditText>(R.id.inputV)
            val numPText = findViewById<EditText>(R.id.inputP)
            val numCText = findViewById<EditText>(R.id.inputC)
            val numHText = findViewById<EditText>(R.id.inputH)
            val numIText = findViewById<EditText>(R.id.inputI)
            val numXText = findViewById<EditText>(R.id.inputX)
            numCText.setText(splited[splited.size-9])
            numDText.setText(splited[splited.size-8])
            numIText.setText(splited[splited.size-7])
            numHText.setText(splited[splited.size-6])
            numPText.setText(splited[splited.size-5])
            numQText.setText(splited[splited.size-4])
            numVText.setText(splited[splited.size-3])
            numXText.setText(splited[splited.size-2])

        }catch (e : NullPointerException){
            val toast = Toast.makeText(applicationContext, "File is empty", Toast.LENGTH_LONG)
            toast.show()
        }
    }

    fun onCheck(view: View){
        val inputCycle = findViewById<EditText>(R.id.inputValueN)
        val checkInputCycle = findViewById<CheckBox>(R.id.inputValueCheck)
        if (checkInputCycle.isChecked){
            inputCycle.isFocusableInTouchMode = true
        }else{
            inputCycle.isFocusable = false
        }
    }

}
