package com.example.advancedcalculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.advancedcalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var lastNumeric = false
    private var stateError = false
    private var lastDot = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun onDigitButtonClick(view: View) {
        if (stateError) {
            binding.firstNumberTextview.text = (view as Button).text
            stateError = false
        } else {
            binding.firstNumberTextview.append((view as Button).text)
        }
        lastNumeric = true
        if (binding.firstNumberTextview.text.last() != '.') {
            onEqual()
        }
    }


    fun onOperatorButtonClick(view: View) {
        if (lastNumeric && !stateError) {
            binding.firstNumberTextview.append((view as Button).text)
            lastNumeric = false
            lastDot = false
            onEqual()
        }
    }


    fun onEqualClick(view: View) {
        if (!binding.firstNumberTextview.text.isNullOrEmpty() && binding.firstNumberTextview.text.last() != '.') {
            onEqual()
            binding.firstNumberTextview.text = binding.resultNumberTextview.text.toString().drop(1)
        }
    }


    fun onBackButtonClick(view: View) {
        if (!binding.firstNumberTextview.text.isNullOrEmpty()) {
            binding.firstNumberTextview.text = binding.firstNumberTextview.text.substring(
                0, binding.firstNumberTextview.text.length - 1
            )
        }
        try {
            val lastChar = binding.firstNumberTextview.text.toString().last()

            if (lastChar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            binding.resultNumberTextview.text = ""
            binding.resultNumberTextview.visibility = View.GONE
            Log.e("last char error", e.toString())
        }
    }


    fun onAllClearButtonClick(view: View) {
        binding.firstNumberTextview.text = ""
        binding.resultNumberTextview.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.resultNumberTextview.visibility = View.GONE

    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val txt = binding.firstNumberTextview.text.toString()
            expression = ExpressionBuilder(txt).build()
            try {
                val result = expression.evaluate()

                binding.resultNumberTextview.visibility = View.VISIBLE

                binding.resultNumberTextview.text = "=$result"
            } catch (ex: ArithmeticException) {
                Log.e("evaluate error", ex.toString())
                binding.resultNumberTextview.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }


}