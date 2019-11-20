package com.example.finalapp.actvities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalapp.actvities.SignUpActivity
import com.example.finalapp.isValidEmail
import com.example.finalapp.isValidPassword
import com.example.finalapp.validate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.finalapp.R.layout.activity_login)

        initComponents()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser == null){
            Toast.makeText(this, "The user isnt logded in", Toast.LENGTH_SHORT).show()
            val myUser = mAuth.currentUser!!
            myUser.displayName
            myUser.email
            myUser.phoneNumber
            myUser.photoUrl
            myUser.isEmailVerified
        } else {
            Toast.makeText(this, "The user is logded in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initComponents() {
        btnSignIn.setOnClickListener{
            val email = edtLayoutEmail.editText?.text.toString()
            val password = edtLayoutPassword.editText?.text.toString()

            if (isValidEmail(email) && isValidPassword(password)){
                signInWithEmailAndPassword(email, password)
            } else {
                Toast.makeText(this, "Wrong user or password", Toast.LENGTH_SHORT).show()
            }
        }

        txtForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        btnNewAccount.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        edtLayoutEmail.validate{
            edtLayoutEmail.editText?.error = if(isValidEmail(it)) null else "Invalid email"
        }

        edtLayoutPassword.validate{
            edtLayoutPassword.editText?.error = if(isValidPassword(it)) null else "Invalid password"
        }
    }

    //Acceder con un usuario creado
    private fun signInWithEmailAndPassword(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if(mAuth.currentUser!!.isEmailVerified){
                        Toast.makeText(this, "The user ${mAuth.currentUser?.displayName} has been logged in", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "First confirm the mail we sent you", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "An unexpected error occurred, please try again later", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Validación de los campos
    private fun isEmailAndPasswordValid(email: String, password: String): Boolean{
        return email.isNotEmpty() && password.isNotEmpty()
    }


}
