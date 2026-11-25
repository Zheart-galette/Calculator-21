package com.example.calculator_21

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    //ประกาศตัวแปร
    private lateinit var display: TextView //แสดงผลลัพธ์ตัวแรก
    private var current = "" //ค่าที่กำลังพิมพ์
    private var operator = "" //เครื่องหมายที่เลือก เช่น +,-,*,/
    private var firstNumber = 0.0 //ตัวเลขแรก
    private var secondNumber = 0.0 //ตัวเลขที่สอง
    private var result = 0.0 //เก็บผลลัพธ์ในการคำนวน
    private var isNewOperation = true //ใช้รีเซ็ตหน้าจอ เมื่อเริ่มการคำนวนใหม่



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //เชื่อม textview เข้ากับ layout
        display = findViewById(R.id.TxtDisplay)

        //กลุ่มปุ่ตัวเลข 0-9
        val numberButton = listOf(
            R.id.btnZero,
            R.id.btnOne,
            R.id.btnTwo,
            R.id.btnThree,
            R.id.btnFour,
            R.id.btnFive,
            R.id.btnSix,
            R.id.btnSeven,
            R.id.btnEight,
            R.id.btnNine
        )

        //กำหนด event ให้ปุ่มตัวเลขทุกปุ่ม
        for (id in numberButton){
            findViewById<Button>(id).setOnClickListener {
                numberClicked((it as Button).text.toString())
            }
        }
        //เชื่อมโยงปุ่มคำนวน + ,-, *, /
        findViewById<Button>(R.id.btnPlus).setOnClickListener { operatorClicked("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { operatorClicked("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { operatorClicked("*") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { operatorClicked("/") }
        findViewById<Button>(R.id.btnMod).setOnClickListener { operatorClicked("%") }
        //เชื่อมโยงปุ่มพิเศษ = ,AC, . ,+-
        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.btnAc).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnDot).setOnClickListener { addDot() }
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener { togglePlusMinus() }
        findViewById<Button>(R.id.btnDel).setOnClickListener { del() }



    }
    //เมื่อกดปุ่มตัวเลข
    private fun numberClicked(value: String){
        if (isNewOperation){
            display.text = ""
            isNewOperation = false
        }
        current += value
        display.text = current
    }
    //เมื่อกดปุ่มเครื่องหมาย
    private fun operatorClicked(op: String){
        if (current.isNotEmpty()){
            firstNumber = current.toDouble()
            operator = op
            current = ""
        }
    }
    //เมื่อกดปุ่มคำนวน =
    private fun calculateResult(){
        if (current.isNotEmpty()){
            secondNumber = current.toDouble()
            result = when (operator){
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "*" -> firstNumber * secondNumber
                "%" -> if (secondNumber != 0.0) (firstNumber % secondNumber) else Double.NaN
                "/" -> if (secondNumber != 0.0) firstNumber / secondNumber else Double.NaN
                else -> 0.0

            }
            display.text = if (result.isNaN()) "Cannot divied by Zero" else result.toString()
            current = result.toString()
            isNewOperation = true
        }
    }
    //ฟังกฺ์ชั่นของปุ่ม AC(all clear)
    private fun clearAll(){
        current = ""
        operator = ""
        firstNumber = 0.0
        secondNumber = 0.0
        result = 0.0
        display.text = "0"
        isNewOperation = true
    }

    //ฟังก์ชั่นเพิ่ม Dot
    private fun addDot(){
        if (!current.contains(".")){
            if (current.isEmpty())current = "0" //ถ้ายังไม่มีเลข ให้ขึ้นต้นด้วย 0
            current += "."
            display.text = current
        }
    }

    //ฟังก์ชั่นของปุ่ม togglePlusMinus สลับค่า
    private fun togglePlusMinus(){
        if (current.isNotEmpty() && current != "0"){
            current  = if (current.startsWith("-")){
                current.substring(1)

            }
            else{
              "-$current"
            }
            display.text = current
        }
    }
    //ฟังก์ชั่นป่มลบที่ละตัว
    private fun del(){
        //ถ้าจอกำลังแสดลงผลลัพธ์ จะไม่ให้ลบทีละตัว
        if(isNewOperation) return

        if(current.isNotEmpty()){
            current = current.dropLast(1) //ตัดตัวสุดท้ายออก

            if (current == "-" || current.isEmpty()){
                current = ""
                display.text = "0"

            }
            else{
              display.text = current
            }
        }
    }
}