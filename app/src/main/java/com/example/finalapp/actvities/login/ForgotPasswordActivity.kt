package com.example.finalapp.actvities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalapp.R
import com.example.finalapp.actvities.utilities.isValidEmail
import com.example.finalapp.actvities.utilities.validate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initComponents()
    }

    private fun initComponents() {

        edtRestorePasswordLayoutEmail.validate {
            edtRestorePasswordLayoutEmail.editText?.error = if (isValidEmail(
                    it
                )
            ) null else "Invalid email"
        }

        btnGoToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        btnResetPassword.setOnClickListener{
            val email = edtRestorePasswordLayoutEmail.editText?.text.toString()
            if (isValidEmail(email)){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    Toast.makeText(this, "An email has been sent to you email to reset your password", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Please make sure the email address is correct", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
