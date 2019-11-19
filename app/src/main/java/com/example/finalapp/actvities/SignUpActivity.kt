package com.example.finalapp.actvities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalapp.actvities.login.LoginActivity
import com.example.finalapp.isValidConfirmPassword
import com.example.finalapp.isValidEmail
import com.example.finalapp.isValidPassword
import com.example.finalapp.validate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.finalapp.R.layout.activity_sign_up)

        initComponsnts()
    }

    private fun initComponsnts() {
        btnGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        btnSignUp.setOnClickListener{
            val email = edtSignUpLayoutEmail.editText!!.text.toString()
            val password = edtSignUpLayoutPassword.editText!!.text.toString()
            val confirmPassword = edtSignUpLayoutConfirmPassword.editText?.text.toString()

            if (isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(password, confirmPassword)){
                signUpByEmail(email, password)
            } else {
                Toast.makeText(this, "Please fill all the boxes", Toast.LENGTH_SHORT).show()
            }
        }

        edtSignUpLayoutEmail.validate{
            edtSignUpLayoutEmail.editText?.error = if(isValidEmail(it)) null else "Invalid email"
        }

        edtSignUpLayoutPassword.validate{
            edtSignUpLayoutPassword.editText?.error = if(isValidPassword(it)) null else "Invalid password"
        }

        edtSignUpLayoutConfirmPassword.validate{
            edtSignUpLayoutConfirmPassword.editText?.error = if(isValidConfirmPassword(edtSignUpLayoutPassword.editText?.text.toString(), it)) null else "Confirmation password don't match with the password"
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser == null){
            Toast.makeText(this, "The user isnt logded in", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "The user is logded in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUpByEmail(email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this
        ) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "An email has been sent to you. Please confirm to sign in.", Toast.LENGTH_SHORT).show()
                val user = mAuth.currentUser
            } else {
                Toast.makeText(this, "An expected error occurred, please try again later", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
