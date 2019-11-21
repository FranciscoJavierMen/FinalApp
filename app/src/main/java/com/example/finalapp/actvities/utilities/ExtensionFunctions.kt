package com.example.finalapp.actvities.utilities

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

    fun TextInputLayout.validate(validation: (String) -> Unit){
        this.editText?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                validation(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    fun isValidEmail(email: String):Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun isValidPassword(password: String):Boolean {
        val passwordPatter = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        val pattern = Pattern.compile(passwordPatter)
        return pattern.matcher(password).matches()
    }

    fun isValidConfirmPassword(password: String, confirmPassword: String):Boolean {
        return password == confirmPassword
    }
