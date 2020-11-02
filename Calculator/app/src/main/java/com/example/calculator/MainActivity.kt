package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    lateinit var label: TextView
    lateinit var button1: Button
    lateinit var button2: Button
    lateinit var button3: Button
    lateinit var button4: Button
    lateinit var button5: Button
    lateinit var button6: Button
    lateinit var button7: Button
    lateinit var button8: Button
    lateinit var button9: Button
    lateinit var button0: Button
    lateinit var buttonPlus: Button
    lateinit var buttonEquals: Button
    lateinit var buttonMinus: Button
    lateinit var buttonDivide: Button
    lateinit var buttonMultiply: Button
    lateinit var buttonDot: Button
    lateinit var buttonReverse: Button
    lateinit var buttonDelete: Button

    var firstNum: Double = 0.0
    var secondNum: Double = 0.0
    var res: Double = 0.0
    var sign: String = ""
    var curExpr: String = ""
    var temp: Int = 0
    var isDot: Boolean = false
    var timer: Int = 1
    var tempDouble: Double = 0.0
    var isPlaceDot: Boolean = false
    val numbers: Array<Double> = arrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        label = findViewById(R.id.Label)
        button1 = findViewById(R.id.Button1)
        button2 = findViewById(R.id.Button2)
        button3 = findViewById(R.id.Button3)
        button4 = findViewById(R.id.Button4)
        button5 = findViewById(R.id.Button5)
        button6 = findViewById(R.id.Button6)
        button7 = findViewById(R.id.Button7)
        button8 = findViewById(R.id.Button8)
        button9 = findViewById(R.id.Button9)
        button0 = findViewById(R.id.Button0)
        buttonPlus = findViewById(R.id.ButtonPlus)
        buttonMinus = findViewById(R.id.ButtonMinus)
        buttonMultiply = findViewById(R.id.ButtonMultiply)
        buttonDivide = findViewById(R.id.ButtonDivide)
        buttonEquals = findViewById(R.id.ButtonEquals)
        //buttonReverse = findViewById(R.id.ButtonReverse)
        buttonDelete = findViewById(R.id.ButtonDelete)
        buttonDot = findViewById(R.id.ButtonDot)

        button1.setOnClickListener {
            updateLabel(1.0)
        }
        button2.setOnClickListener {
            updateLabel(2.0)
        }
        button3.setOnClickListener {
            updateLabel(3.0)
        }
        button4.setOnClickListener {
            updateLabel(4.0)
        }
        button5.setOnClickListener {
            updateLabel(5.0)
        }
        button6.setOnClickListener {
            updateLabel(6.0)
        }
        button7.setOnClickListener {
            updateLabel(7.0)
        }
        button8.setOnClickListener {
            updateLabel(8.0)
        }
        button9.setOnClickListener {
            updateLabel(9.0)
        }
        button0.setOnClickListener {
            updateLabel(0.0)
        }
        buttonPlus.setOnClickListener {
            updateLabel("plus")
        }
        buttonMinus.setOnClickListener {
            updateLabel("minus")
        }
        buttonDivide.setOnClickListener {
            updateLabel("divide")
        }
        buttonMultiply.setOnClickListener {
            updateLabel("multiply")
        }
        buttonEquals.setOnClickListener {
            updateLabel("equals")
        }
        /*buttonReverse.setOnClickListener {
            updateLabel("reverse")
        }*/
        buttonDelete.setOnClickListener {
            updateLabel("delete")
        }
        buttonDot.setOnClickListener {
            updateLabel("dot")
        }
    }

    private fun updateLabel(cur: Any) {
        if (cur is Double) {
            if (firstNum == -1.0) {
                firstNum = if (isDot) {
                    cur * 0.1
                } else {
                    cur
                }
            } else if (secondNum == -1.0 && sign != "") {
                secondNum = if (isDot) {
                    cur * 0.1
                } else {
                    cur
                }
            } else if (firstNum != -1.0 && sign == "") {
                if (!isDot) {
                    firstNum *= 10
                    firstNum += cur
                } else {
                    tempDouble = cur
                    for (i in 0 until timer) {
                        tempDouble *= 0.1
                    }
                    timer++
                    firstNum += tempDouble
                }
            } else {
                if (!isDot) {
                    secondNum *= 10
                    secondNum += cur
                } else {
                    tempDouble = cur
                    for (i in 0 until timer) {
                        tempDouble *= 0.1
                    }
                    timer++
                    secondNum += tempDouble
                }
            }
            if (sign == "") {
                curExpr = label.text as String
                if (curExpr == "0") {
                    if (cur in numbers) {
                        temp = cur.toInt()
                        curExpr = "$temp"
                    }
                } else {
                    if (cur in numbers) {
                        temp = cur.toInt()
                        curExpr += "$temp"
                    }
                }
                label.text = curExpr
            } else {
                curExpr = label.text as String
                if (curExpr == "0") {
                    if (cur in numbers) {
                        temp = cur.toInt()
                        curExpr = "$temp"
                    }
                } else {
                    if (cur in numbers) {
                        temp = cur.toInt()
                        curExpr += "$temp"
                    }
                }
                label.text = curExpr
            }
        } else if (cur is String) {
            when (cur) {
                "plus" -> plus()
                "minus" -> minus()
                "divide" -> divide()
                "multiply" -> multiply()
                "reverse" -> reverse()
                "delete" -> delete()
                "dot" -> dot()
                "equals" -> equals()
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    fun deleteLastSign(): String {
        var newCurExpr: String = ""
        for (i in 0 until curExpr.length - 1) {
            newCurExpr += curExpr[i]
        }
        return newCurExpr
    }

    fun plus(): Unit {
        isDot = false
        timer = 1
        isPlaceDot = false
        if (sign == "") {
            sign = "+"
            curExpr = label.text as String + "+"
            label.text = curExpr
        } else {
            sign = "+"
            curExpr = deleteLastSign()
            curExpr += "+"
            label.text = curExpr
        }
    }

    fun minus(): Unit {
        isDot = false
        timer = 1
        isPlaceDot = false
        if (sign == "") {
            sign = "-"
            curExpr = label.text as String + "-"
            label.text = curExpr
        } else {
            sign = "-"
            curExpr = deleteLastSign()
            curExpr += "-"
            label.text = curExpr
        }
    }

    fun divide(): Unit {
        isDot = false
        timer = 1
        isPlaceDot = false
        if (sign == "") {
            sign = "/"
            curExpr = label.text as String + "/"
            label.text = curExpr
        } else {
            sign = "/"
            curExpr = deleteLastSign()
            curExpr += "/"
            label.text = curExpr
        }
    }

    fun multiply(): Unit {
        isDot = false
        timer = 1
        isPlaceDot = false
        if (sign == "") {
            sign = "*"
            curExpr = label.text as String + "*"
            label.text = curExpr
        } else {
            sign = "*"
            curExpr = deleteLastSign()
            curExpr += "*"
            label.text = curExpr
        }
    }

    fun equals(): Unit {
        isDot = false
        timer = 1
        if (secondNum != -1.0) {
            if (sign == "/" && secondNum == 0.0) {
                firstNum = 0.0
                secondNum = 0.0
                res = 0.0
                curExpr = ""
                sign = ""
            }
            when (sign) {
                "+" -> res = firstNum + secondNum
                "-" -> res = firstNum - secondNum
                "/" -> res = firstNum / secondNum
                "*" -> res = firstNum * secondNum
            }
            if (res.toInt().toDouble() == res) {
                temp = res.toInt()
                label.text = "$temp"
            } else {
                res = String.format("%.2f", res).toDouble()
                label.text = "$res"
            }
            sign = ""
            curExpr = ""
            firstNum = res
            secondNum = -1.0
            res = 0.0
            timer = 1
            isPlaceDot = false
        }
    }

    fun reverse(): Unit {
        if (secondNum == -1.0 && sign == "") {
            firstNum = -firstNum
            if (firstNum.toInt().toDouble() == firstNum) {
                temp = firstNum.toInt()
                curExpr = "$temp"
            } else {
                curExpr = "$firstNum"
            }
            label.text = curExpr
        }
    }

    fun delete(): Unit {
        isDot = false
        timer = 1
        firstNum = -1.0
        secondNum = -1.0
        res = 0.0
        curExpr = ""
        label.text = ""
        sign = ""
        isPlaceDot = false
    }

    fun dot(): Unit {
        if (!isPlaceDot) {
            curExpr = label.text as String
            curExpr += "."
            isDot = true
            timer = 1
            label.text = curExpr
            isPlaceDot = true
        }
    }
}