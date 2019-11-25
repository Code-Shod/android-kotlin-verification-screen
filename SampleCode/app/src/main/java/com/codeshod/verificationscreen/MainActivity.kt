package com.codeshod.verificationscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), TextWatcher {

    //    This is the code that we will get from the previous screen to compare here
    private var verificationCode: String? = "12345"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    //    Initialization function to set up the basic things at the starting of the activity
    private fun init() {
        codeOne.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        codeOne.addTextChangedListener(this)
        codeTwo.addTextChangedListener(this)
        codeThree.addTextChangedListener(this)
        codeFour.addTextChangedListener(this)
        codeFive.addTextChangedListener(this)

//        get the verification code from the previous activity if required
//        verificationCode = intent.extras?.getString("verificationCode")

        codeFive.tag = true
    }

    //    We have override a function from the Activity, whenever we press any key there are two states
//    First, when we press the key and our finger is touching the key, its calls onKeyDown function
//    Second, when we press the key and remove our finger, its calls onKeyUp function
//    We are using the onKeyUp function
//    This function works only for the Hard keyboard keys but not for the software keyboard keys.
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            when {
                codeTwo.hasFocus() -> {
                    getBackFocus(codeOne)
                    codeTwo.isFocusableInTouchMode = false

                }
                codeThree.hasFocus() -> {
                    getBackFocus(codeTwo)
                    codeThree.isFocusableInTouchMode = false
                }
                codeFour.hasFocus() -> {
                    getBackFocus(codeThree)
                    codeFour.isFocusableInTouchMode = false
                }
                codeFive.hasFocus() -> {
                    if (codeFive.tag as Boolean) {
                        getBackFocus(codeFour)
                        codeFive.isFocusableInTouchMode = false
                    } else {
                        codeFive.tag = true
                    }
                }
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    //    Text change listener, whenever user will enter or remove anything from the editText this listener will be called
    override fun afterTextChanged(s: Editable?) {
        if (s!!.isNotEmpty()) {
            when {
                codeFive.hasFocus() -> {
                    codeFive.tag = false
                    isCodeValid()
                }
                codeFour.hasFocus() -> {
                    getNextFocus(codeFive)
                    codeFour.isFocusableInTouchMode = false
                }
                codeThree.hasFocus() -> {
                    getNextFocus(codeFour)
                    codeThree.isFocusableInTouchMode = false
                }
                codeTwo.hasFocus() -> {
                    getNextFocus(codeThree)
                    codeTwo.isFocusableInTouchMode = false
                }
                codeOne.hasFocus() -> {
                    getNextFocus(codeTwo)
                    codeOne.isFocusableInTouchMode = false
                }
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    //    When user enters the code and we move the focus from one editText to the next one.
    private fun getNextFocus(editText: EditText) {
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
    }


    //    When user presses the delete button to remove the code and we move the focus back to the previous editText.
    private fun getBackFocus(editText: EditText) {
        editText.isFocusableInTouchMode = true
        editText.setText("")
        editText.requestFocus()
    }

    private fun isCodeValid() {
//        combine the entered code
        val verifyCode: String = codeOne.text.toString() + codeTwo.text.toString() +
                codeThree.text.toString() + codeFour.text.toString() + codeFive.text.toString()

//        Compare the entered code with the original code locally to proceed.
//        Or Call the API for comparing the code.
        if (verifyCode == verificationCode) {
            Toast.makeText(this, R.string.string_successful, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.string_unsuccessful, Toast.LENGTH_SHORT).show()
        }
    }
}