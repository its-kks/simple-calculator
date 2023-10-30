package com.example.simplestcalculator.kks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.Stack
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var displayAns: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayAns = findViewById(R.id.displayAns)

        val oneBtn = findViewById<Button>(R.id.one)
        oneBtn.setOnClickListener { appendTextToDisplay("1") }

        val twoBtn = findViewById<Button>(R.id.two)
        twoBtn.setOnClickListener { appendTextToDisplay("2") }

        val threeBtn = findViewById<Button>(R.id.three)
        threeBtn.setOnClickListener { appendTextToDisplay("3") }

        val plusBtn = findViewById<Button>(R.id.plus)
        plusBtn.setOnClickListener { appendTextToDisplay("+") }

        val fourBtn = findViewById<Button>(R.id.four)
        fourBtn.setOnClickListener { appendTextToDisplay("4") }

        val fiveBtn = findViewById<Button>(R.id.five)
        fiveBtn.setOnClickListener { appendTextToDisplay("5") }

        val sixBtn = findViewById<Button>(R.id.six)
        sixBtn.setOnClickListener { appendTextToDisplay("6") }

        val minusBtn = findViewById<Button>(R.id.minus)
        minusBtn.setOnClickListener { appendTextToDisplay("-") }

        val sevenBtn = findViewById<Button>(R.id.seven)
        sevenBtn.setOnClickListener { appendTextToDisplay("7") }

        val eightBtn = findViewById<Button>(R.id.eight)
        eightBtn.setOnClickListener { appendTextToDisplay("8") }

        val nineBtn = findViewById<Button>(R.id.nine)
        nineBtn.setOnClickListener { appendTextToDisplay("9") }

        val multiplyBtn = findViewById<Button>(R.id.multiply)
        multiplyBtn.setOnClickListener { appendTextToDisplay("*") }

        val zeroBtn = findViewById<Button>(R.id.zero)
        zeroBtn.setOnClickListener { appendTextToDisplay("0") }

        val equalToBtn = findViewById<Button>(R.id.equalTo)
        equalToBtn.setOnClickListener { calculateResult() }

        val acBtn = findViewById<Button>(R.id.ac)
        acBtn.setOnClickListener { clearDisplay() }

        val divideBtn = findViewById<Button>(R.id.divide)
        divideBtn.setOnClickListener { appendTextToDisplay("/") }

        val powBtn = findViewById<Button>(R.id.pow)
        powBtn.setOnClickListener { appendTextToDisplay("^") }

        val rightParBtn = findViewById<Button>(R.id.rightPar)
        rightParBtn.setOnClickListener { appendTextToDisplay(")") }

        val leftParBtn = findViewById<Button>(R.id.leftPar)
        leftParBtn.setOnClickListener { appendTextToDisplay("(") }

        val erase = findViewById<Button>(R.id.erase)
        erase.setOnClickListener { erase() }

    }
    private fun erase() {
        val currentText = displayAns.text.toString()
        if (currentText.isNotEmpty()) {
            displayAns.text = currentText.substring(0, currentText.length - 1)
        }
    }


    private fun appendTextToDisplay(text: String) {
        var currentText = displayAns.text.toString()
        if (currentText.contains("Invalid Input")) {
            currentText = currentText.replace("Invalid Input", "")
        }
        displayAns.text = currentText + text
    }


    private fun calculateResult() {
        try {
            val currentText = displayAns.text.toString()
            val infixList = convert(currentText)
            val postfixList = toPostFix(infixList)
            val ans = evaluatePostfix(postfixList)
            displayAns.text = "" + ans
        }
        catch (e : Exception){
            displayAns.text = "Invalid Input"
        }

    }

    private fun clearDisplay() {
        displayAns.text = ""
    }

    private fun convert(expression: String): ArrayList<String> {
        val ans = ArrayList<String>()
        val valBuilder = StringBuilder()

        for (i in 0 until expression.length) {
            val c = expression[i]
            if (c == '(' || c == ')' || c == '+' ||
                c == '-' || c == '*' || c == '/' || c == '^'
            ) {
                if (valBuilder.isNotEmpty()) {
                    ans.add(valBuilder.toString())
                    valBuilder.clear()
                }
                ans.add(c.toString())
            } else {
                valBuilder.append(c)
            }
        }

        if (valBuilder.isNotEmpty()) {
            ans.add(valBuilder.toString())
        }

        return ans
    }

    private fun toPostFix(infixList: ArrayList<String>): ArrayList<String> {
        val postfixList = ArrayList<String>()
        val stack = Stack<String>()

        val precedence = mapOf(
            "+" to 1,
            "-" to 1,
            "*" to 2,
            "/" to 2,
            "^" to 3
        )

        for (token in infixList) {
            if (token.matches(Regex("[0-9]+"))) {
                postfixList.add(token)
            } else if (token == "(") {
                stack.push(token)
            } else if (token == ")") {
                while (!stack.isEmpty() && stack.peek() != "(") {
                    postfixList.add(stack.pop())
                }
                if (!stack.isEmpty() && stack.peek() == "(") {
                    stack.pop() // Pop the "("
                }
            } else {
                while (!stack.isEmpty() && precedence.getOrDefault(token, 0) <= precedence.getOrDefault(stack.peek(), 0)) {
                    postfixList.add(stack.pop())
                }
                stack.push(token)
            }
        }

        while (!stack.isEmpty()) {
            postfixList.add(stack.pop())
        }

        return postfixList
    }
    private fun calculate(val1 : Int, val2 :Int, oper : String):Int{
        when(oper){
            "+" -> return val1 + val2
            "-" -> return val1 - val2
            "*" -> return val1 * val2
            "/" -> return val1 / val2
            else -> {
                return Math.pow(val1.toDouble(),val2.toDouble()).toInt()
            }
        }
    }
    private fun evaluatePostfix(postfixList : ArrayList<String>): Int{
        var set = HashSet<String>()
        set.add("+")
        set.add("-")
        set.add("*")
        set.add("/")
        set.add("^")

        val stack = Stack<Int>()

        for(curr in postfixList){
            if(set.contains(curr)){
                val v2 : Int = stack.pop()
                val v1 : Int = stack.pop()
                var ans = calculate(v1,v2,curr)
                stack.push(ans)
            }
            else{
                stack.push(Integer.valueOf(curr))
            }
        }
        return stack.pop()
    }
}
